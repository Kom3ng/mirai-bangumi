import java.io.IOException;

import org.abstruck.miraibangumi.util.BgmRequestBuilder;
import org.abstruck.miraibangumi.util.Context;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Astrack
 * @date 2023/8/13
 */
public class BgmApiTest {
    @Test
    public void testCalendarApi() throws IOException{
        Request request = BgmRequestBuilder.INSTANCE.authCreate("/mono_search/test?cat=all",Context.INSTANCE.bgmToken())
        .header("Cookie", "chii_searchDateLine="+System.currentTimeMillis())
        .removeHeader("User-Agent").addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36 Edg/115.0.1901.203").get().build();
        try(Response response = Context.INSTANCE.httpClient().newCall(request).execute()){
            String body = response.body().string();
            Document doc = Jsoup.parse(body);

            System.out.println(body);

            Element searchResult = doc.getElementById("columnSearchB");
            searchResult.getElementsByClass("1").forEach(e -> {
                System.out.println(e.text());
            });
        }
    }
}
