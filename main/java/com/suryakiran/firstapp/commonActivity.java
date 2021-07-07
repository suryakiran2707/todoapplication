package com.suryakiran.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class commonActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener,
        DialogBox2.DialogListener {
    private ArrayList<Upload> items ;

    private MyAdapter adapter;
    private FirebaseStorage storage;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private ListView listView ;
    private ProgressBar circle;
    private ValueEventListener DBListener;
    private SearchView searchView;
    private ArrayList<Upload> results ;
    private MenuItem menuItem;
    public static ArrayList<String> catNames= new ArrayList();
    String catName;

    public void showActivity(View view){
        Intent intent = new Intent(com.suryakiran.myapplication.commonActivity.this,add_item.class);
        intent.putExtra("catName",catName);
        startActivityForResult(intent,101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode==RESULT_OK){
            setHandler();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.new_action_bar);
        TextView title= getSupportActionBar().getCustomView().findViewById(R.id.title);
        catName=getIntent().getStringExtra("title");
        title.setText(getIntent().getStringExtra("title"));

        listView= findViewById(R.id.listView);
        circle= findViewById(R.id.progressCircle);
        items= new ArrayList<>();
        auth= FirebaseAuth.getInstance();
        storage= FirebaseStorage.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("users/"+auth.getCurrentUser().getUid());


        adapter= new MyAdapter(com.suryakiran.myapplication.commonActivity.this,R.layout.my_list_item,items);
        listView.setAdapter(adapter);
        adapter.setOnItemClickListener(com.suryakiran.myapplication.commonActivity.this);

        DBListener =databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                catNames.clear();
                items.clear();
                for(DataSnapshot category : snapshot.getChildren()){
                    if(category.getKey().equals(catName) ){
                        System.out.println(category.getKey());
                        for(DataSnapshot data : category.getChildren()){

                            Upload upload = data.getValue(Upload.class);
                            upload.setKey(data.getKey());
                            upload.setCategory(category.getKey());
                            items.add(upload);
                        }
                    }
                    if(!category.getKey().equals("credentials") && !category.getKey().equals("dashboard")){

                        catNames.add(category.getKey());
                    }

                }
                adapter.notifyDataSetChanged();
                circle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                circle.setVisibility(View.INVISIBLE);
                Toast.makeText(com.suryakiran.myapplication.commonActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });




    }



    @Override
    public void deleteItemClick(int position) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure!")
                .setMessage("Item will be deleted permanently")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Upload selectedItem;
                        if(searchView!=null && !searchView.isIconified()){
                            selectedItem = results.get(position);
                        }else{

                            selectedItem = items.get(position);

                        }

                        String key = selectedItem.getKey();
                        StorageReference imageRef=null;

                        if(!selectedItem.getImageUrl().equals("https://acadianakarate.com/wp-content/uploads/2017/04/default-image.jpg")){
                            imageRef = storage.getReferenceFromUrl(selectedItem.getImageUrl());}


                        if(imageRef!=null){
                            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    databaseReference.child(selectedItem.getCategory()+"/"+key).removeValue();
                                    setHandler();
                                    Toast.makeText(com.suryakiran.myapplication.commonActivity.this,"item deleted successfully",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            databaseReference.child(selectedItem.getCategory()+"/"+key).removeValue();
                            setHandler();
                            Toast.makeText(com.suryakiran.myapplication.commonActivity.this,"item deleted successfully",Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("No",null)
                .show();
    }

    @Override
    public void optionsItemClick(int position) {

        DialogBox2 dialogBox= new DialogBox2();
        dialogBox
                .show(getSupportFragmentManager(), String.valueOf(position));

    }

    @Override
    public void shareItemClick(int position) {
        ImageDownloader task = new ImageDownloader();
        try {
            String note = items.get(position).getNote();
            String Expiry= items.get(position).getExpiryDate();
            String imageurl= items.get(position).getImageUrl();
            if(Expiry.isEmpty())
                Expiry="nothing";
            if(note.isEmpty())
                note="nothing";
            Bitmap bitmap = task.execute(imageurl).get();
            String shareText= "Category : "+items.get(position).getCategory()+"\n" +
                    "Name : "+items.get(position).getName()+"\n" +
                    "Note : "+note+ "\n" +
                    "Quantity : "+items.get(position).getQuantity()+"\n" +
                    "Expiry date : "+ Expiry;
            String sub= imageurl.substring(imageurl.lastIndexOf("."),imageurl.lastIndexOf("?"));
            File file= new File(G.context.getExternalCacheDir(),"temp"+sub);
            FileOutputStream fileOutputStream =new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            file.setReadable(true,false);

            //share
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
            Uri photoUrl= FileProvider.getUriForFile(G.context,G.context.getApplicationContext().getPackageName()+".provider",file);
            shareIntent.putExtra(Intent.EXTRA_STREAM,photoUrl);
            shareIntent.setType("image/jpeg");
            shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(shareIntent,null));
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(com.suryakiran.myapplication.commonActivity.this,"Failed to share",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(DBListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        menuItem= menu.findItem(R.id.searchMenu);
        searchView = ((SearchView) menuItem.getActionView());
        searchView.setBackgroundColor(R.drawable.unsplash);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                querySubmitOrChange(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                querySubmitOrChange(s);
                return true;
            }


        });


        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                searchView.setIconifiedByDefault(false);
                results= new ArrayList<>(items);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                if(searchView!=null && !searchView.isIconified()){
                    searchView.setQuery("",true);
                    Log.i("kjfbkhf","hjffhf");
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void querySubmitOrChange(String s){
        results = new ArrayList<>();
        for(Upload x: items){
            if(x.getName().contains(s) || x.getNote().contains(s) || x.getQuantity().contains(s)){
                results.add(x);
            }
        }
        ((MyAdapter)listView.getAdapter()).update(results);
    }


    @Override
    public void applyText(String quantity,String position) {
        int pos= Integer.parseInt(position);
        if(searchView!=null && !searchView.isIconified()){
            databaseReference.child(results.get(pos).getCategory()+"/"+results.get(pos).getKey()+"/quantity").setValue(quantity);
        }else{
            databaseReference.child(items.get(pos).getCategory()+"/"+items.get(pos).getKey()+"/quantity").setValue(quantity);
        }
        setHandler();
    }

    private void setHandler(){
        if((searchView!=null && !searchView.isIconified())  ){
            CharSequence s=  searchView.getQuery();
            String temp="";
            if(s.length()==0){
                temp="a";
            }
            searchView.setQuery(temp,true);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    searchView.setQuery(s,true);
                }
            },1);
        }

    }

    private class ImageDownloader extends AsyncTask<String ,Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}

