package org.abstruck.miraibangumi.util;

import net.mamoe.mirai.utils.MiraiLogger;
import okhttp3.OkHttpClient;
import org.abstruck.miraibangumi.MiraiBangumi;
import org.abstruck.miraibangumi.data.Infobox;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Astrack
 * @date 2023/8/14
 */
public class Context {
    public static final Context INSTANCE = new Context();
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final String BGM_API_SERVER = "https://api.bgm.tv";
    private static final String BGM_SERVER = "https://bgm.tv";
    private static final Gson GSON;
    private static final String BGM_TOKEN = "cIT7cg2nIfIpDqKhvHzJptna23sSJRq4KLsGrp5X";

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Infobox.class, new Infobox());
        GSON = gsonBuilder.create();
    }

    public OkHttpClient httpClient(){
        return HTTP_CLIENT;
    }
    public String bgmApiServer(){
        return BGM_API_SERVER;
    }
    public MiraiLogger logger(){
        return MiraiBangumi.INSTANCE.getLogger();
    }

    public Gson gson(){
        return GSON;
    }

    public String bgmToken(){
        return BGM_TOKEN;
    }

    public String bgmServer(){
        return BGM_SERVER;
    }
}
