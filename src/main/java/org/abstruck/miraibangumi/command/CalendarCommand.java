package org.abstruck.miraibangumi.command;

import com.google.gson.Gson;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.MiraiLogger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.abstruck.miraibangumi.MiraiBangumi;
import org.abstruck.miraibangumi.data.BangumiItem;
import org.abstruck.miraibangumi.data.Calender;
import org.abstruck.miraibangumi.util.BgmRequestBuilder;
import org.abstruck.miraibangumi.util.Context;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

/**
 * @author Astrack
 * @date 2023/8/14   
*/
public class CalendarCommand extends JRawCommand {
    private OkHttpClient httpClient = Context.INSTANCE.httpClient();
    private MiraiLogger logger = Context.INSTANCE.logger();
    public CalendarCommand() {
        super(MiraiBangumi.INSTANCE, "jrfs","今日放送");
        setDescription("今日放送的动画番组");
        setUsage("(/)jrfs or (/)今日放送");
        setPrefixOptional(true);
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull MessageChain args) {
        Contact subject = sender.getSubject();

        if (subject == null){
            return;
        }

        Request request = BgmRequestBuilder.INSTANCE
                .apiCreate("/calendar")
                .get()
                .build();
        try (Response response = httpClient.newCall(request).execute()){
            if (response.isSuccessful()){
                Calender[] calenders = new Gson().fromJson(response.body().string(), Calender[].class);
                int i1 = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                if (i1 == 1){
                    i1 = 8;
                }
                Calender calender = calenders[i1-2];
                StringBuilder sb = new StringBuilder(calender.weekday().cn() + "\n\n");
                for (int i = 0; i < calender.items().length; i++) {
                    BangumiItem item = calender.items()[i];
                    String score = "--";
                    if (item.getRating() != null && item.getRating().getTotal()>10){
                        score = item.getRating().getScore().toString();
                    }
                    sb.append(item.getName())
                            .append("    ")
                            .append("bgm评分:")
                            .append(score)
                            .append("\n");
                }
                sb.deleteCharAt(sb.length()-1);
                subject.sendMessage(new PlainText(sb.toString()));
            }else {
                logger.info("request failed");
                logger.warning(response.message());
            }
        } catch (Exception e) {
            logger.warning(e);
        }
    }
}
