package com.thukuma.youtube_playlist;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.thukuma.youtube_playlist.Models.PlayListModels;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetPlaylist {
    private Context context;
    private String url;

    public GetPlaylist(Context context,String url,onComplete onComplete,onError error) {
        this.context = context;
        this.url = url;
        AndroidNetworking.initialize(context);
        AndroidNetworking.post("https://converterbear.org/playlist-converter.php")
                .addBodyParameter("entry",url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Document doc = Jsoup.parse(response);
                        Elements elements = doc.select("div[class=media]");
                        for (Element e : elements){
                            String img = e.select("img[class=media-object]").attr("src");
                            String title = e.select("strong[class=media-heading]").text();
                            String link = e.select("button[name=entry]").attr("value");
                            PlayListModels models = new PlayListModels();
                            AndroidNetworking.get("https://dirpy.com/studio?url="+link+"&utm_source=freeonlinevideodownloader&utm_medium=form")
                                    .build()
                                    .getAsString(new StringRequestListener() {
                                        @Override
                                        public void onResponse(String response) {
                                            Document doc = Jsoup.parse(response);
                                            String video_url = doc.select("video[class=center-block]").select("source").attr("src");
                                            String video_thum = doc.select("video[class=center-block]").attr("poster");
                                            String video_title = doc.select("div[class=col-xs-10]").select("input[class=form-control]").attr("value");

                                            if (!video_url.isEmpty()&&!video_thum.isEmpty()&&!video_title.isEmpty()){
                                                models.setImage("https:"+img);
                                                models.setTitle("("+title);
                                                models.setUrl(video_url);
                                                onComplete.onComplete(models);
                                            }

                                        }

                                        @Override
                                        public void onError(ANError anError) {
                                            error.onError(anError.toString());
                                        }
                                    });


                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        error.onError(anError.toString());
                    }
                });

    }
    public interface onComplete{
        void onComplete(PlayListModels models);
    }
    public interface onError{
        void onError(String onError);
    }
}
