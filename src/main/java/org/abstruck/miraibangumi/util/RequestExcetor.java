package org.abstruck.miraibangumi.util;

import java.util.function.Consumer;

import net.mamoe.mirai.console.command.CommandSender;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestExcetor {
    private Executor executor;
    private Consumer<Exception> exceptionHandler;
    private Request request;
    private OkHttpClient httpClient;

    public RequestExcetor(OkHttpClient httpClient,Request request){
        this.httpClient = httpClient;
        this.request = request;
    }

    public RequestExcetor excetor(Executor executor){
        this.executor = executor;
        return this;
    }

    public RequestExcetor exceptionHandler(Consumer<Exception> exceptionHandler){
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    public RequestExcetor defualtExceptionHandler(CommandSender commandSender){
        this.exceptionHandler = (e) -> {
            Context.INSTANCE.logger().warning(e);
            commandSender.getSubject().sendMessage(e.getMessage());
        };
        //TODO 只对管理员返回错误信息
        return this;
    }

    public void execute(){
        try(Response response = httpClient.newCall(request).execute()){
            if (executor !=null){
                executor.accept(response);
            }
        } catch(Exception e){
            if(exceptionHandler != null){
                exceptionHandler.accept(e);
            }
        }
    }
    public void executeIfSuccess(){
        try(Response response = httpClient.newCall(request).execute()){
            if (executor !=null){
                if(response.isSuccessful()){
                    executor.accept(response);
                }else{
                    throw new Exception(response.message());
                }
            }
        } catch(Exception e){
            if(exceptionHandler != null){
                exceptionHandler.accept(e);
            }
        }
    }
}
