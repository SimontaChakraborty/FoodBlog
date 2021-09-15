package edu.ewubd.foodblog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class HomeActivity extends Activity {

    private ListView lvPost;
    private ArrayList<Posts> posts;
    private CustomPostActivity postsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setContentView(R.layout.activity_home);
        Database db = new Database(this);

        lvPost = findViewById(R.id.lvPost);

        findViewById(R.id.btnCreateNew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, PostActivity.class);
                startActivity(i);
            }
        });
        findViewById(R.id.btnProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.btnexit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HomeActivity.this.finish();
                HomeActivity.this.finishAffinity();
                //System.exit(0);
            }
        });

        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.clearCurrentUser();
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        initializePosts();
    }
    private void initializePosts(){
        Database db2 = new Database(this);
        KeyValueDB db = new KeyValueDB(this);
        Cursor rows = db.execute("SELECT * FROM key_value_pairs");
        if(rows.getCount() == 0) {
            return;
        }

        posts = new ArrayList<>();
        while(rows.moveToNext()) {
            String key = rows.getString(0);
            String postsData = rows.getString(1);
            String[] fieldValues = postsData.split(":-;-:");
            String title = fieldValues[0];
            String desc = fieldValues[1];
            String userMail = "";
            String dateTime = "";
            String img = "";
            if(fieldValues.length>2){
                img = fieldValues[2];
                userMail = fieldValues[3];
                dateTime = fieldValues[4];
            }
            Posts p = new Posts(key,title,desc,img, userMail, dateTime);
            posts.add(p);
        }
        db.close();

        postsActivity = new CustomPostActivity(this, posts);
        lvPost.setAdapter(postsActivity);

//        lvPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
//                // String item = (String) parent.getItemAtPosition(position);
//                System.out.println(position);
//                Intent i = new Intent(HomeActivity.this, PostActivity.class);
//                i.putExtra("PostKey", posts.get(position).key);
//                startActivity(i);
//            }
//        });
        lvPost.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(posts.get(position).userMail);
                if(posts.get(position).userMail.equals(db2.getCurrentUser())){
                    String message = "Do you want to delete Post - "+ posts.get(position).title+" ?";
                    showDialog(message,"Delete Post",position);
                    return false;
                }else return false;
            }

        });

    }
    private void showDialog(String message, String title,int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Uncomment the below code to Set the message and title from the strings.xml file
        builder.setMessage(message);
        builder.setTitle(title);
        //Setting message manually and performing action on button click
        builder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Util.getInstance().deleteByKey(HomeActivity.this,posts.get(position).key);
                        dialog.cancel();
                        initializePosts();
                        postsActivity.notifyDataSetChanged();
                        //lvEvents.notifyAll();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        //alert.setTitle("Error Dialog");
        alert.show();
    }

}