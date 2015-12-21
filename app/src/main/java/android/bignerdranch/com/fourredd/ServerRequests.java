package android.bignerdranch.com.fourredd;

/**
 * Allows the app to communicate with the database set up at hostinger.uk.co
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import java.io.IOException;
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

    public void storeThreadLikeDataInBackground(Thread thread, GetThreadCallback threadCallBack){
        mProgressDialog.show();
        new StoreThreadLikeDataAsyncTask(thread, threadCallBack).execute();

    }

    public void storeCommentDataInBackground(Comment comment, GetCommentCallback commentCallback){
        mProgressDialog.show();
        new StoreCommentDataAsyncTask(comment, commentCallback).execute();

    }

    public void storeLocationDataInBackground(LocationObject locationObject, GetLocationCallback locationCallback){
        mProgressDialog.show();
        new StoreLocationDataAsyncTask(locationObject, locationCallback).execute();

    }


    public void fetchUserDataInBackground(User user, GetUserCallback callback){
        mProgressDialog.show();
        new fetchUserDataAsyncTask(user, callback).execute();

    }

    public void fetchForumInBackground(Forum forum, GetForumCallback callback){
        mProgressDialog.show();
        new fetchForumDataAsyncTask(forum, callback).execute();

    }

    public void fetchCommentForumInBackground(CommentForum commentForum, Thread thread, GetCommentForumCallback callback){
        mProgressDialog.show();
        new fetchCommentForumDataAsyncTask(commentForum, thread, callback).execute();

    }

    public void fetchLocationForumInBackground(LocationForum locationForum, GetLocationForumCallback locationForumCallback){
        mProgressDialog.show();
        new fetchLocationForumDataAsyncTask(locationForum, locationForumCallback).execute();

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
            Log.d("ADebugTag", "1Register Fail!!!!!!!!!!!!!!!!!!!!!!!!!: \n");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);


            } catch (Exception e) {
                Log.d("ADebugTag", "Register Fail!!!!!!!!!!!!!!!!!!!!!!!!!: \n");
                e.printStackTrace();
            }
            return null;
        }

//        try {
//            post.setEntity(new UrlEncodedFormEntity(dataToSend));
//
//            HttpResponse httpResponse = client.execute(post);
//
//            HttpEntity entity = httpResponse.getEntity();
//            String result = EntityUtils.toString(entity);
//
//
//
////                Log.d("ADebugTag", "Value: Thread!!!!!!!!!!!!!!!!!!!!!!! \n\n\n" + result);
//            JSONObject jsonObject = new JSONObject(result);
//
////                Log.d("Register", "ValueJSON: \n\n\n\n\n" + result);
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        @Override
        protected void onPostExecute(Void aVoid){

            mProgressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }


    public class StoreLocationDataAsyncTask extends AsyncTask<Void, Void, Void> {
        LocationObject mLocationObject;
        GetLocationCallback mGetLocationCallback;
        public StoreLocationDataAsyncTask(LocationObject locationObject, GetLocationCallback locationCallback){
            this.mLocationObject = locationObject;
            this.mGetLocationCallback = locationCallback;
        }

        @Override
        protected Void doInBackground(Void...params){

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("user", mLocationObject.user));
            dataToSend.add(new BasicNameValuePair("latitude", mLocationObject.latitude+""));
            dataToSend.add(new BasicNameValuePair("longitude", mLocationObject.longitude+""));


            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "AddLocation.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);

            } catch (Exception e) {
                Log.d("ADebugTag", "Register Fail: \n");
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid){

            mProgressDialog.dismiss();
            mGetLocationCallback.done(null);
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



//                Log.d("ADebugTag", "Value: \n" + result);
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



            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchForumData.php");

            Forum returnedForum = new Forum();
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);



//                Log.d("ADebugTag", "JSON RETURN: \n" + result);
                JSONArray jsonArray = new JSONArray(result);

//                Log.d("ADebugTag", "JSON ARRAY!!!!!: \n" + jsonArray);


                if(jsonArray.length() ==0){
                    returnedForum = null;
                }else{
                    int i = 0;
                    for(i=0; i<jsonArray.length(); i++){
                        Thread tempThread = new Thread();


                        JSONObject object = jsonArray.getJSONObject(i);
                        tempThread.user = object.get("user").toString();
                        tempThread.title = object.get("title").toString();
                        tempThread.text = object.get("text").toString();
                        tempThread.like = object.getInt("num_like");
                        tempThread.id = object.getInt("threadID");
                        tempThread.dislikes = object.getInt("num_dislikes");




//                        Log.d("ADebugTag", "AHHHHHHHH!!!!!!!!!!!!!!: \n" + tempThread.title);
                        returnedForum.addThread(tempThread);


 //

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


    public class fetchLocationForumDataAsyncTask extends AsyncTask<Void, Void, LocationForum> {
        LocationForum mLocationForum;
        GetLocationForumCallback mLocationForumCallback;

        public fetchLocationForumDataAsyncTask(LocationForum forum, GetLocationForumCallback forumCallback) {
            this.mLocationForum = forum;
            this.mLocationForumCallback = forumCallback;
        }

        @Override
        protected LocationForum doInBackground(Void... voids) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();



            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchLocationForumData.php");

            LocationForum returnedForum = new LocationForum();
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);



//                Log.d("ADebugTag", "JSON RETURN: \n" + result);
                JSONArray jsonArray = new JSONArray(result);

//                Log.d("ADebugTag", "JSON ARRAY!!!!!: \n" + jsonArray);


                if(jsonArray.length() ==0){
                    returnedForum = null;
                }else{
                    int i = 0;
                    for(i=0; i<jsonArray.length(); i++){
                        LocationObject tempObject = new LocationObject();


                        JSONObject object = jsonArray.getJSONObject(i);
                        tempObject.user = object.get("username").toString();
                        tempObject.latitude = object.getDouble("latitude");
                        tempObject.longitude = object.getDouble("longitude");

//                        Log.d("ADebugTag", "AHHHHHHHH!!!!!!!!!!!!!!: \n" + tempObject.user);
                        returnedForum.addLocation(tempObject);


                        //

                    }

                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return returnedForum;
        }

        @Override
        protected void onPostExecute(LocationForum returnedForum){

            mProgressDialog.dismiss();
            try {
                mLocationForumCallback.done(returnedForum);
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.onPostExecute(returnedForum);
        }
    }




    public class fetchCommentForumDataAsyncTask extends AsyncTask<Void, Void, CommentForum> {
        CommentForum mCommentForum;
        GetCommentForumCallback mCommentForumCallback;
        Thread mThread;

        public fetchCommentForumDataAsyncTask(CommentForum commentForum, Thread thread, GetCommentForumCallback commentForumCallback) {
            this.mCommentForum = commentForum;
            this.mCommentForumCallback = commentForumCallback;
            this.mThread = thread;
        }

        @Override
        protected CommentForum doInBackground(Void... voids) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("currentThreadID", mThread.id+""));
//            Log.d("ADebugTag", "MTHREAD ID!!!!!: \n" + mThread.id);



            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchCommentForumData.php");

            CommentForum returnedCommentForum = new CommentForum();
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);



//                Log.d("ADebugTag", "JSON RETURN: \n" + result);
                JSONArray jsonArray = new JSONArray(result);

//                Log.d("ADebugTag", "JSON ARRAY!!!!!: \n" + jsonArray);


                if(jsonArray.length() ==0){
                    returnedCommentForum = new CommentForum();
                }else{
                    int i = 0;
                    for(i=0; i<jsonArray.length(); i++){
                        Comment tempComment = new Comment();


                        JSONObject object = jsonArray.getJSONObject(i);
                        tempComment.user = object.get("user").toString();
                        tempComment.text = object.get("text").toString();
                        tempComment.like = object.getInt("num_like");
                        tempComment.threadID = object.getInt("threadID");
                        tempComment.commentID = object.getInt("commentID");




//                        Log.d("ADebugTag", "COMMENTS!!!!!!!!!!!!!!: \n" + tempComment.text);
                        returnedCommentForum.addComment(tempComment);


                        //

                    }

                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return returnedCommentForum;
        }

        @Override
        protected void onPostExecute(CommentForum returnedCommentForum){

            mProgressDialog.dismiss();
            mCommentForumCallback.done(returnedCommentForum);
            super.onPostExecute(returnedCommentForum);
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
            dataToSend.add(new BasicNameValuePair("like", mThread.like + ""));
            dataToSend.add(new BasicNameValuePair("dislikes", mThread.dislikes + ""));



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



//                Log.d("ADebugTag", "Value: Thread!!!!!!!!!!!!!!!!!!!!!!! \n\n\n" + result);
                JSONObject jsonObject = new JSONObject(result);

//                Log.d("Register", "ValueJSON: \n\n\n\n\n" + result);



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


    public class StoreThreadLikeDataAsyncTask extends AsyncTask<Void, Void, Void> {
        Thread mThread;
        GetThreadCallback threadCallback;
        public StoreThreadLikeDataAsyncTask(Thread thread, GetThreadCallback threadCallback){
            this.mThread = thread;
            this.threadCallback = threadCallback;
        }

        @Override
        protected Void doInBackground(Void...params){

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("threadID", mThread.id+""));
            dataToSend.add(new BasicNameValuePair("user", mThread.user));
            dataToSend.add(new BasicNameValuePair("title", mThread.title));
            dataToSend.add(new BasicNameValuePair("text", mThread.text));
            dataToSend.add(new BasicNameValuePair("like", mThread.like + ""));
            dataToSend.add(new BasicNameValuePair("dislikes", mThread.dislikes+""));



            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UpdateThread.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));

                HttpResponse httpResponse = client.execute(post);

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



    public class StoreCommentDataAsyncTask extends AsyncTask<Void, Void, Void> {
        Comment mComment;
        GetCommentCallback mCommentCallback;
        public StoreCommentDataAsyncTask(Comment comment, GetCommentCallback commentCallback){
            this.mComment = comment;
            this.mCommentCallback = commentCallback;
        }

        @Override
        protected Void doInBackground(Void...params){

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("threadID", mComment.threadID+""));
            dataToSend.add(new BasicNameValuePair("user", mComment.user));
            dataToSend.add(new BasicNameValuePair("text", mComment.text));
            dataToSend.add(new BasicNameValuePair("num_like", mComment.like+""));



            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "NewComment.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));

                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);



//                Log.d("ADebugTag", "Value: COMMENT!!!!!!!!!!!!!!!!!!!!!!! \n\n\n" + result);
                JSONObject jsonObject = new JSONObject(result);



            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid){

            mProgressDialog.dismiss();
            mCommentCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }



}
