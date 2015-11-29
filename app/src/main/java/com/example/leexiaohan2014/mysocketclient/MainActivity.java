package com.example.leexiaohan2014.mysocketclient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends ActionBarActivity {


    EditText ip;
    EditText port;
    EditText editText;
    TextView text;
    String ipaddress = null;
    String portaddress = null;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip = (EditText) findViewById(R.id.ip);
        port = (EditText) findViewById(R.id.port);
        editText = (EditText) findViewById(R.id.edit);
        text = (TextView) findViewById(R.id.text);

        findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ipaddress = ip.getText().toString();
                portaddress = port.getText().toString();
                if (!((ipaddress.isEmpty())||(portaddress.isEmpty()))) {
                    connect();
                }
                else {
                    text.append("-------IP or PORT empty-------\n");
                }

            }
        });

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

    }
    //-------------------------------------------------------------------

    Socket socket = null;
    BufferedWriter writer = null;
    BufferedReader reader = null;
    public void connect() {


        AsyncTask<Void, String,Void> read = new AsyncTask<Void, String, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        socket = new Socket(ipaddress, Integer.parseInt(portaddress));
                        //socket = new Socket(ip.getText().toString(),12345);
                        writer = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
                        reader = new BufferedReader(new InputStreamReader(socket.getInputStream())) ;
                        publishProgress("@success");
                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this,"can not connect",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }


                    try {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            publishProgress(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    if (values[0].equals("@success")) {
                        Toast.makeText(MainActivity.this, "connect successfully", Toast.LENGTH_SHORT).show();
                    }

                    text.append("text received from server"+values[0]+"\n");
                    super.onProgressUpdate(values);

                }
            };
            read.execute();

    }

    public void send() {
        try {
            text.append("text sent from client: "+editText.getText().toString() + "\n");
            writer.write(editText.getText().toString()+"\n");
            writer.flush();
            editText.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    //-------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
