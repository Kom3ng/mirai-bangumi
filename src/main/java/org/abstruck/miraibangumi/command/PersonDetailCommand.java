package org.abstruck.miraibangumi.command;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.abstruck.miraibangumi.MiraiBangumi;
import org.abstruck.miraibangumi.data.KVMap;
import org.abstruck.miraibangumi.data.KVMap.KV;
import org.abstruck.miraibangumi.data.Person;
import org.abstruck.miraibangumi.data.RelatedCharacter;
import org.abstruck.miraibangumi.data.RelatedSubject;
import org.abstruck.miraibangumi.data.StringOrderedMap;
import org.abstruck.miraibangumi.data.VMap;
import org.abstruck.miraibangumi.data.VMap.V;
import org.abstruck.miraibangumi.util.BangumiRequestBuilder;
import org.abstruck.miraibangumi.util.BgmRequestBuilder;
import org.abstruck.miraibangumi.util.BloodTypeUtil;
import org.abstruck.miraibangumi.util.Context;
import org.abstruck.miraibangumi.util.array.ClassifyUtils;
import org.abstruck.miraibangumi.util.mirai.MessageUtils;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import okhttp3.Request;
import okhttp3.Response;

public class PersonDetailCommand extends JCompositeCommand {

    public PersonDetailCommand() {
        super(MiraiBangumi.INSTANCE,"person");
    }
    
    @SubCommand
    public void info(CommandSender commandSender,int id){
        BangumiRequestBuilder.create()
            .apiPath("/v0/persons/"+id)
            .getMethod()
            .buildToExecutor()
            .excetor(response -> {
                Person person = Context.INSTANCE.gson().fromJson(response.body().string(), Person.class);
                ForwardMessageBuilder forwardMessageBuilder = new ForwardMessageBuilder(commandSender.getSubject());
                
                

                forwardMessageBuilder.setDisplayStrategy(MessageUtils.simpleDisplayStategy(
                    person.name(), 
                    List.of("收藏数: "+person.stat().collects(),"评论数: "+person.stat().comments()), 
                    "最后编辑: "+person.last_modified()));


                forwardMessageBuilder.add(commandSender.getBot(),new PlainText(person.name()));
                if(person.images() != null){
                    try(ExternalResource resource = ExternalResource.create(new URL(person.images().medium()).openStream())){
                        Image image = commandSender.getSubject().uploadImage(resource);
                        forwardMessageBuilder.add(commandSender.getBot(),image);
                    }
                }

                ForwardMessageBuilder summaryForwardMessageBuilder = new ForwardMessageBuilder(commandSender.getSubject());
                MessageUtils.addInForwardMessageBuilder(commandSender.getBot(), summaryForwardMessageBuilder, person.summary());
                summaryForwardMessageBuilder.setDisplayStrategy(MessageUtils.simpleDisplayStategy(
                    "XXX", 
                    List.of("\u7B80\u4ECB"), 
                    "XXX"));

                forwardMessageBuilder.add(commandSender.getBot(),summaryForwardMessageBuilder.build());

                if(person.infobox() != null){
                    Arrays.stream(person.infobox())
                        .forEach(i -> {
                            if (i instanceof StringOrderedMap sMap){
                                forwardMessageBuilder.add(commandSender.getBot(),new PlainText(sMap.key+":"+sMap.value));

                            } else if (i instanceof KVMap kvMap){
                                StringBuilder valueBuilder = new StringBuilder();
                                valueBuilder.append(kvMap.key).append(":\n\n");
                                for (KV value : kvMap.value) {
                                    valueBuilder.append(value.k()).append(':').append(value.v()).append('\n');
                                }
                                valueBuilder.deleteCharAt(valueBuilder.length()-1);
                                forwardMessageBuilder.add(commandSender.getBot(), new PlainText(valueBuilder.toString()));
                            } else if (i instanceof VMap vMap){
                                StringBuilder valueBuilder = new StringBuilder();
                                valueBuilder.append(vMap.key).append(":\n\n");
                                for (V value : vMap.value) {
                                    valueBuilder.append(value.v()).append('\n');
                                }
                                valueBuilder.deleteCharAt(valueBuilder.length()-1);
                                forwardMessageBuilder.add(commandSender.getBot(), new PlainText(valueBuilder.toString()));
                            }
                        });
                }

                if(person.blood_type()!=null){
                    forwardMessageBuilder.add(commandSender.getBot(), new PlainText("血型:"+BloodTypeUtil.byId(person.blood_type())));
                }

                Integer year = person.birth_year();
                Integer mon =  person.birth_mon();
                Integer day = person.birth_day();

                StringBuilder birthBuilder = new StringBuilder();
                if(year!=null){
                    birthBuilder.append(year).append("年");
                }
                if(mon!=null){
                    birthBuilder.append(mon).append("月");
                }
                if(day!=null){
                    birthBuilder.append(day).append("日");
                }
                if(!birthBuilder.isEmpty()){
                    forwardMessageBuilder.add(commandSender.getBot(), new PlainText("出生日期: "+birthBuilder.toString()));
                }

                commandSender.getSubject().sendMessage(forwardMessageBuilder.build());
            })
            .defualtExceptionHandler(commandSender)
            .executeIfSuccess();         
    }

    @SubCommand
    public void contribute(CommandSender commandSender,int id){
        Person person = null;

        Request presonInfoRequest = BangumiRequestBuilder.create().apiPath("/v0/persons/"+id).getMethod().build();

        try(Response response = Context.INSTANCE.httpClient().newCall(presonInfoRequest).execute()){
            if(response.isSuccessful()){
                person = Context.INSTANCE.gson().fromJson(response.body().string(), Person.class);
            }
        } catch (IOException e){
            commandSender.sendMessage(e.getMessage());
            Context.INSTANCE.logger().warning(e);
        } 

        final Person p = person;

        BangumiRequestBuilder.create()
            .apiPath("/v0/persons/%d/subjects".formatted(id))
            .getMethod()
            .buildToExecutor()
            .excetor(response -> {
                RelatedSubject[] relatedSubjects = Context.INSTANCE.gson().fromJson(response.body().string(), RelatedSubject[].class);
            
                ForwardMessageBuilder forwardMessageBuilder = new ForwardMessageBuilder(commandSender.getSubject());
                Map<String,List<RelatedSubject>> classifiedRelatedSubjects = ClassifyUtils.classify(relatedSubjects,RelatedSubject::staff);
                MessageUtils.mapListStringForwardMessageAdderLimited(classifiedRelatedSubjects, forwardMessageBuilder,commandSender.getBot(), s->s.id()+" "+s.name());

                forwardMessageBuilder.setDisplayStrategy(
                    MessageUtils.simpleDisplayStategy(
                        "%s参与制作的作品".formatted(p == null?"unknown":p.name()),
                        List.of("",""),
                        "共%d部作品".formatted(relatedSubjects.length))
                );

                commandSender.sendMessage(forwardMessageBuilder.build());
            })
            .defualtExceptionHandler(commandSender)
            .executeIfSuccess();
    }

    @SubCommand
    public void character(CommandSender commandSender,int id){
        Person person = null;

        Request presonInfoRequest = BgmRequestBuilder.INSTANCE.apiCreate("/v0/persons/"+id)
            .get()
            .build();

        try(Response response = Context.INSTANCE.httpClient().newCall(presonInfoRequest).execute()){
            if(response.isSuccessful()){
                person = Context.INSTANCE.gson().fromJson(response.body().string(), Person.class);
            }
        } catch (IOException e){
            commandSender.sendMessage(e.getMessage());
            Context.INSTANCE.logger().warning(e);
        } 
        final Person finalPerson = person;
        BangumiRequestBuilder.create()
            .apiPath("/v0/persons/%d/characters".formatted(id))
            .getMethod()
            .buildToExecutor()
            .excetor(response -> {
                RelatedCharacter[] relatedCharacters = Context.INSTANCE.gson().fromJson(response.body().string(), RelatedCharacter[].class);
                ForwardMessageBuilder forwardMessageBuilder = new ForwardMessageBuilder(commandSender.getSubject());
                Map<String,List<RelatedCharacter>> classifiedRelatedCharactersMap =  ClassifyUtils.classify(relatedCharacters,RelatedCharacter::name);
                
                MessageUtils.mapListStringForwardMessageAdder(classifiedRelatedCharactersMap, forwardMessageBuilder, commandSender.getBot(), RelatedCharacter::subject_name);

                forwardMessageBuilder.setDisplayStrategy( MessageUtils.simpleDisplayStategy(
                    "%s参与配音的人物".formatted(finalPerson == null?"unknown":finalPerson.name()), 
                    List.of("",""), 
                    "共%d位人物".formatted(relatedCharacters.length)));

                commandSender.sendMessage(forwardMessageBuilder.build());
            })
            .defualtExceptionHandler(commandSender)
            .executeIfSuccess();
    }
}
