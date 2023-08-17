package org.abstruck.miraibangumi.command;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.abstruck.miraibangumi.MiraiBangumi;
import org.abstruck.miraibangumi.data.CharacterInfo;
import org.abstruck.miraibangumi.data.KVMap;
import org.abstruck.miraibangumi.data.KVMap.KV;
import org.abstruck.miraibangumi.data.StringOrderedMap;
import org.abstruck.miraibangumi.data.VMap;
import org.abstruck.miraibangumi.data.VMap.V;
import org.abstruck.miraibangumi.util.BangumiRequestBuilder;
import org.abstruck.miraibangumi.util.BloodTypeUtil;
import org.abstruck.miraibangumi.util.Context;
import org.abstruck.miraibangumi.util.StringUtils;
import org.abstruck.miraibangumi.util.mirai.MessageUtils;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

public class CharacterDetailCommand extends JCompositeCommand {

    public CharacterDetailCommand() {
        super(MiraiBangumi.INSTANCE,"character");
    }
    
    @SubCommand
    public void info(CommandSender commandSender,int id){
        BangumiRequestBuilder.create()
            .apiPath("/v0/characters/"+id)
            .getMethod()
            .buildToExecutor()
            .excetor(response -> {
                CharacterInfo character = Context.INSTANCE.gson().fromJson(response.body().string(), CharacterInfo.class);
                ForwardMessageBuilder forwardMessageBuilder = new ForwardMessageBuilder(commandSender.getSubject());
                
                

                forwardMessageBuilder.setDisplayStrategy(
                    MessageUtils.simpleDisplayStategy(
                    character.name(), 
                    List.of("收藏数: "+character.stat().collects(),"评论数: "+character.stat().comments()), 
                    "ID: "+character.id()));

                forwardMessageBuilder.add(commandSender.getBot(),new PlainText(character.name()));

                if(character.images() != null){
                    try(ExternalResource resource = ExternalResource.create(new URL(character.images().medium()).openStream())){
                        Image image = commandSender.getSubject().uploadImage(resource);
                        forwardMessageBuilder.add(commandSender.getBot(),image);
                    }
                }

                ForwardMessageBuilder summaryForwardMessageBuilder = new ForwardMessageBuilder(commandSender.getSubject());
                for (String str : StringUtils.qqLimit(character.summary())) {
                    summaryForwardMessageBuilder.add(commandSender.getBot(),new PlainText(str));
                }



                summaryForwardMessageBuilder.setDisplayStrategy(MessageUtils.simpleDisplayStategy(
                    "XXX",
                    List.of("\u7B80\u4ECB"),
                    "XXX")
                );
                forwardMessageBuilder.add(commandSender.getBot(),summaryForwardMessageBuilder.build());

                if(character.infobox() != null){
                    Arrays.stream(character.infobox())
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

                if(character.blood_type()!=null){
                    forwardMessageBuilder.add(commandSender.getBot(), new PlainText("血型:"+BloodTypeUtil.byId(character.blood_type())));
                }

                Integer year = character.birth_year();
                Integer mon =  character.birth_mon();
                Integer day = character.birth_day();

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
}
