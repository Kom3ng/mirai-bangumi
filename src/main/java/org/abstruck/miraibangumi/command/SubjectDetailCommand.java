package org.abstruck.miraibangumi.command;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.abstruck.miraibangumi.MiraiBangumi;
import org.abstruck.miraibangumi.data.BangumiItem;
import org.abstruck.miraibangumi.data.BangumiSubject;
import org.abstruck.miraibangumi.data.RelatedPerson;
import org.abstruck.miraibangumi.data.SearchData;
import org.abstruck.miraibangumi.data.SubjectRelatedCharacter;
import org.abstruck.miraibangumi.runtime.SearchResultManager;
import org.abstruck.miraibangumi.util.BangumiRequestBuilder;
import org.abstruck.miraibangumi.util.BgmRequestBuilder;
import org.abstruck.miraibangumi.util.Context;
import org.abstruck.miraibangumi.util.array.ClassifyUtils;
import org.abstruck.miraibangumi.util.mirai.MessageUtils;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import okhttp3.Request;
import okhttp3.Response;

public class SubjectDetailCommand extends JCompositeCommand {
    public SubjectDetailCommand() {
        super(MiraiBangumi.INSTANCE,"detail");
    }

    @SubCommand
    public void info(CommandSender commandSender,int index){
        SearchData lastSearch = SearchResultManager.INSTANCE.getLastSearc(commandSender.getSubject());
        if (lastSearch == null){
            commandSender.getSubject().sendMessage(new PlainText("请先搜索"));
            return;
        }

        try{
            if (index>lastSearch.result().list().length||index<1){
                throw new ArrayIndexOutOfBoundsException("索引不正确");
            }
            BangumiItem item = lastSearch.result().list()[index-1];

            Request request = BgmRequestBuilder.INSTANCE.apiCreate("/v0/subjects/"+item.getId()).get().build();

            try(Response response = Context.INSTANCE.httpClient().newCall(request).execute()){
                if (response.isSuccessful()){
                    BangumiSubject subject = Context.INSTANCE.gson().fromJson(response.body().string(),BangumiSubject.class);
                
                    ExternalResource res = ExternalResource.create(new URL(subject.images().small()).openStream());
                    Image uploadImage = commandSender.getSubject().uploadImage(res);

                    String follow = """
                                     
                 
                                     中文名: %s
                                     话数: %d
                                     简介: %s
                                     放送日期: %s
                                     bgm评分: %s
                                     rank: %d
                                     id: %d
                                     """.formatted(
                                         subject.name_cn(),
                                         subject.total_episodes(),
                                         subject.summary(),
                                         subject.date(),
                                         subject.rating().total() < 10 ? "--" : subject.rating().score().toString(),
                                         subject.rating().rank(),
                                         subject.id()
                                     );
                    
                    MessageChain messageChain = new MessageChainBuilder()
                                            .append(new PlainText(subject.name()+"\n"))
                                            .append(uploadImage)
                                            .append(new PlainText(follow))
                                            .build();

                    commandSender.getSubject().sendMessage(messageChain);

                    res.close();
                } else {
                    commandSender.sendMessage(response.message());
                }
            }
        } catch (Exception e){
            Context.INSTANCE.logger().warning(e);
            commandSender.getSubject().sendMessage(e.getMessage());
            return;
        } 
    }

    @SubCommand
    public void staff(CommandSender commandSender,int index){
        SearchData lastSearch = SearchResultManager.INSTANCE.getLastSearc(commandSender.getSubject());
        if (lastSearch == null){
            commandSender.getSubject().sendMessage(new PlainText("请先搜索"));
            return;
        }

        if (index>lastSearch.result().list().length||index<1){
            commandSender.getSubject().sendMessage("索引不正确");
        }       
        BangumiItem item = lastSearch.result().list()[index-1];
        BangumiRequestBuilder.create()
            .apiPath("/v0/subjects/"+item.getId()+"/persons")
            .getMethod()
            .buildToExecutor()
            .excetor(response -> {
                RelatedPerson[] relatedPersons = Context.INSTANCE.gson().fromJson(response.body().string(),RelatedPerson[].class);
                
                Map<String,List<RelatedPerson>> relationMap = ClassifyUtils.classify(relatedPersons, p->p.relation());
                ForwardMessageBuilder forwardMessageBuilder = new ForwardMessageBuilder(commandSender.getSubject());
                  
                forwardMessageBuilder.setDisplayStrategy(MessageUtils.simpleDisplayStategy(
                    item.getName(), 
                    List.of("职员表"), 
                    "共%d人".formatted(relatedPersons.length)));
                
                MessageUtils.mapListStringForwardMessageAdder(
                    relationMap, 
                    forwardMessageBuilder, 
                    commandSender.getBot(), 
                    p->p.id()+" "+p.name());
                commandSender.getSubject().sendMessage(forwardMessageBuilder.build());
            })
            .defualtExceptionHandler(commandSender);
    }

    @SubCommand
    public void character(CommandSender commandSender,int index){
        SearchData lastSearch = SearchResultManager.INSTANCE.getLastSearc(commandSender.getSubject());
        if (lastSearch == null){
            commandSender.getSubject().sendMessage(new PlainText("请先搜索"));
            return;
        }

        BangumiItem item = lastSearch.result().list()[index-1];

        BangumiRequestBuilder.create()
            .apiPath("/v0/subjects/%d/characters".formatted(item.getId()))
            .getMethod()
            .buildToExecutor()
            .excetor((response) -> {
                SubjectRelatedCharacter[] characters = Context.INSTANCE.gson().fromJson(response.body().string(), SubjectRelatedCharacter[].class);
        
                Map<String,List<SubjectRelatedCharacter>> classifiedCharacterMap = ClassifyUtils.classify(characters,(c)->c.relation());

                ForwardMessageBuilder forwardMessageBuilder = new ForwardMessageBuilder(commandSender.getSubject());

                MessageUtils.mapListStringForwardMessageAdder(
                    classifiedCharacterMap, 
                    forwardMessageBuilder, 
                    commandSender.getBot(), 
                    c->c.id()+" "+c.name());

                forwardMessageBuilder.setDisplayStrategy(MessageUtils.simpleDisplayStategy(
                    item.getName(), 
                    List.of("角色表"),
                    "共%d人".formatted(characters.length)));

                commandSender.getSubject().sendMessage(forwardMessageBuilder.build());
            })
            .defualtExceptionHandler(commandSender)
            .executeIfSuccess();
    }
}