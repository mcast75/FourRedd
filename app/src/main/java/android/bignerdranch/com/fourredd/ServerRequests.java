package android.bignerdranch.com.fourredd;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mike on 11/9/15.
 */
public class ServerRequests {

    ProgressDialog mProgressDialog;
    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADDRESS = "http://fourredd.esy.es/";

    //public static final String SERVER_ADDRESS = "http://10.0.2.2:80/webservice/";


    public ServerRequests(Context context){

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Processing");
        mProgressDialog.setMessage("Please_wait");
    }

    public void storeUserDataInBackground(User user, GetUserCallback userCallback){
        mProgressDialog.show();
        new StoreUserDataAsyncTask(user, userCallback).execute();
    }

    public void storeThreadDataInBackground(Thread thread, GetThreadCallback threadCallBack){
        mProgressDialog.show();
        new StoreThreadDataAsyncTask(thread, threadCallBack).execute();

    }


    public void fetchUserDataInBackground(User user, GetUserCallback callback){
        mProgressDialog.show();
        new fetchUserDataAsyncTask(user, callback).execute();

    }

    public void fetchForumInBackground(Forum forum, GetForumCallback callback){
        mProgressDialog.show();
        new fetchForumDataAsyncTask(forum, callback).execute();

    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        User mUser;
        GetUserCallback userCallback;
        public StoreUserDataAsyncTask(User user, GetUserCallback userCallback){
            this.mUser = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void...params){

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("name", mUser.name));
            dataToSend.add(new BasicNameValuePair("username", mUser.username));
            dataToSend.add(new BasicNameValuePair("password", mUser.password));


            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);


                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);



                Log.d("ADebugTag", "Value: \n" + result);
                JSONObject jsonObject = new JSONObject(result);



            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid){

            mProgressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }



    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User mUser;
        GetUserCallback userCallback;

        public fetchUserDataAsyncTask(User user, GetUserCallback userCallback) {
            this.mUser = user;
            this.userCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... voids) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("name", mUser.name));
            dataToSend.add(new BasicNameValuePair("username", mUser.username));
            dataToSend.add(new BasicNameValuePair("password", mUser.password));


            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserData.php");

            User returnedUser = null;
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);



                Log.d("ADebugTag", "Value: \n" + result);
                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() ==0){
                    returnedUser = null;
                }else{
                    String name = jsonObject.getString("name");

                    returnedUser = new User(name, mUser.username, mUser.password);

                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser){

            mProgressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }



    public class fetchForumDataAsyncTask extends AsyncTask<Void, Void, Forum> {
        Forum mForum;
        GetForumCallback mForumCallback;

        public fetchForumDataAsyncTask(Forum forum, GetForumCallback forumCallback) {
            this.mForum = forum;
            this.mForumCallback = forumCallback;
        }

        @Override
        protected Forum doInBackground(Void... voids) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            Thread tempThread = new Thread();

            dataToSend.add(new BasicNameValuePair("user", tempThread.user));
            dataToSend.add(new BasicNameValuePair("title", tempThread.title));
            dataToSend.add(new BasicNameValuePair("text", tempThread.text));
            dataToSend.add(new BasicNameValuePair("num_like", tempThread.like+""));



            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserData.php");

            Forum returnedForum = null;
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);



                Log.d("ADebugTag", "JSON RETURN: \n" + result);
                JSONArray jsonArray = new JSONArray(result);

                Log.d("ADebugTag", "JSON ARRAY!!!!!: \n" + jsonArray);


                if(jsonArray.length() ==0){
                    returnedForum = null;
                }else{
                    int i = 0;
                    for(i=0; i<jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);
                        returnedForum.addThread(tempThread);
                        Log.d("ADebugTag", "FORUM!!!!!!!!!!!!!!: \n" + returnedForum);



                    }

                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return returnedForum;
        }

        @Override
        protected void onPostExecute(Forum returnedForum){

            mProgressDialog.dismiss();
            mForumCallback.done(returnedForum);
            super.onPostExecute(returnedForum);
        }
    }




    public class StoreThreadDataAsyncTask extends AsyncTask<Void, Void, Void> {
        Thread mThread;
        GetThreadCallback threadCallback;
        public StoreThreadDataAsyncTask(Thread thread, GetThreadCallback threadCallback){
            this.mThread = thread;
            this.threadCallback = threadCallback;
        }

        @Override
        protected Void doInBackground(Void...params){

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("user", mThread.user));
            dataToSend.add(new BasicNameValuePair("title", mThread.title));
            dataToSend.add(new BasicNameValuePair("text", mThread.text));
            dataToSend.add(new BasicNameValuePair("like", mThread.like+""));


            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "NewThread.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));

                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);



                Log.d("ADebugTag", "Value: Thread!!!!!!!!!!!!!!!!!!!!!!! \n\n\n" + result);
                JSONObject jsonObject = new JSONObject(result);

                Log.d("Register", "ValueJSON: \n\n\n\n\n" + result);



            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid){

            mProgressDialog.dismiss();
            threadCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }






}
