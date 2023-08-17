package org.abstruck.miraibangumi.util.mirai;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.abstruck.miraibangumi.util.StringUtils;

import net.mamoe.mirai.contact.UserOrBot;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.RawForwardMessage;
import net.mamoe.mirai.message.data.ForwardMessage.DisplayStrategy;

public class MessageUtils {
    public static ForwardMessageBuilder addInForwardMessageBuilder(UserOrBot userOrBot,ForwardMessageBuilder builder,String[] strs){
        for (String s : strs) {
            builder.add(userOrBot, new PlainText(s));
        }
        return builder;
    }

    public static ForwardMessageBuilder addInForwardMessageBuilder(UserOrBot userOrBot,ForwardMessageBuilder builder,String str){
        return addInForwardMessageBuilder(userOrBot, builder, StringUtils.qqLimit(str));
    }

    public static DisplayStrategy simpleDisplayStrategy(Function<RawForwardMessage,String> title,Function<RawForwardMessage,List<String>> preview,Function<RawForwardMessage,String> summary){
        return new DisplayStrategy(){
                @Override
                public String generateTitle(RawForwardMessage forward){
                    return title.apply(forward);
                }

                @Override
                public List<String> generatePreview(RawForwardMessage forward){
                    return preview.apply(forward);
                }

                @Override
                public String generateSummary(RawForwardMessage forward){
                    return summary.apply(forward);
                }
        };
    }

    public static DisplayStrategy simpleDisplayStategy(String title,List<String> preview,String summary){
        return simpleDisplayStrategy(f->title, f->preview, f->summary);
    }

    public static DisplayStrategy simpleDisplayStategy(Supplier<String> title,Supplier<List<String>> preview,Supplier<String> summary){
        return simpleDisplayStrategy(f->title.get(), f->preview.get(), f->summary.get());
    }

    public static <V> void mapListStringForwardMessageAdder(Map<String,List<V>> map,ForwardMessageBuilder forwardMessageBuilder,UserOrBot user,Function<V,String> strParser){
        map.forEach((k,vs) -> {
            forwardMessageBuilder.add(user, new PlainText(StringUtils.listStringGenerate(vs,k,v->strParser.apply(v))));
        });
    }
    public static <V> void mapListStringForwardMessageAdderLimited(Map<String,List<V>> map,ForwardMessageBuilder forwardMessageBuilder,UserOrBot user,Function<V,String> strParser){
        map.forEach((k,vs) -> {
            for (String str : StringUtils.qqLimit(StringUtils.listStringGenerate(vs,k,strParser::apply))) {
                forwardMessageBuilder.add(user, new PlainText(str));
            }
        });
    }
}
