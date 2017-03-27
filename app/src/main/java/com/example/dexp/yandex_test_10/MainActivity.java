package com.example.dexp.yandex_test_10;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    EditText txt_in,txt_out;
    public static String LOG_TAG = "my_log";

    boolean selectLang;
    String lang,ranst;
    YandexAPITask yAPI;

    String str_out,str_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("лол  чебурек");

        txt_in = (EditText) findViewById(R.id.txt_in);
        txt_out = (EditText) findViewById(R.id.txt_out);

        lang = "ru";//Значение поумолчанию для параметра lang

        txt_in.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                yAPI = new YandexAPITask();

                // Прописываем то, что надо выполнить после изменения текста
                str_in = txt_in.getText().toString();
                yAPI.execute();//запускаем поток для отлавливания ввода польхователя

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }
    //Поток для перевода текста
    class YandexAPITask extends AsyncTask<Void, String, Void> {

        //Метод для перевода текста
        //Входные параметры:
        //lang - направление перевода ru/en
        //input - текст для превода
        public String translate(String lang, String input) throws IOException {
            String urlStr = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20170315T153359Z.a962c009327b3da6.bcd595deb3f6fcd94460b5b88148ea940b84cbb3";
            URL urlObj = new URL(urlStr);
            HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes("text=" + URLEncoder.encode(input, "UTF-8") + "&lang=" + lang+"&options=1");

            InputStream response = connection.getInputStream();
            String json = new java.util.Scanner(response).nextLine();
            int start = json.indexOf("[");
            int end = json.indexOf("]");
            String translated = json.substring(start + 2, end - 1);
            return translated;
        }


        //Вызывается при запуске потока
        //метод execute();
        @Override
        protected Void doInBackground(Void... params) {

            try {
                str_out = translate(lang, str_in).toString();
                System.out.println("Перевод: "+ translate(lang, str_in));

            } catch (IOException e) {
                e.printStackTrace();
                Log.i(e.getClass().getName(), e.getMessage());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            txt_out.setText(str_out);
        }
    }
}
