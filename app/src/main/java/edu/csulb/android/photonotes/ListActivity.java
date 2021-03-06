package edu.csulb.android.photonotes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    public static String DEBUG_TAG = "ListActivity";
    ArrayList<String> notes = new ArrayList<String>();
    PhotoData photoData;
    ListView listViewNotes;
    String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }

        photoData = new PhotoData(getApplicationContext());
        notes.addAll(photoData.getPhotoCaption());
        //Add List Adapter for Notes Application
        listViewNotes = (ListView) findViewById(R.id.listView);
        listViewNotes.setAdapter(new NotesAdapter());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPhoto = new Intent(getApplicationContext(), AddPhotoActivity.class);
                startActivity(addPhoto);
            }
        });
        listViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent viewPhoto = new Intent(getApplicationContext(), ViewPhotoActivity.class);
                Intent viewPhoto = new Intent(getApplicationContext(), ViewPhotoActivity.class);
                String selectedFromList = (String) (listViewNotes.getItemAtPosition(position));
                Log.d(DEBUG_TAG, "Selected Item position " + position + "Selected Item: " + selectedFromList);
                //viewPhoto.setAction(Intent.ACTION_VIEW);
                uri = photoData.getPhotoURI(selectedFromList);
                viewPhoto.putExtra("imagePath", uri);
                Log.d(DEBUG_TAG, "Selected URI " + uri);
                viewPhoto.putExtra("caption", selectedFromList);
                startActivity(viewPhoto);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Add only new notes
        ArrayList<String> notesLocal = new ArrayList<>();
        notesLocal.addAll(photoData.getPhotoCaption());
        int size = notesLocal.size();
        if (notesLocal.size() > notes.size()) {
            notes.add(notesLocal.get(size - 1));
        }
        //Add List Adapter for Notes Application
        listViewNotes.setAdapter(new NotesAdapter());
    }

    @Override
    public void finish() {
        super.finish();
        //Close any open database connections
        photoData.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(findViewById(R.id.content_list), "Camera Permission Required", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Uri packageURI = Uri.parse("package:edu.csulb.android.photonotes");
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            startActivity(uninstallIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class NotesAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return notes.size();
        }

        @Override
        public String getItem(int position) {
            return notes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return notes.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.content_list, container, false);
            }

            ((TextView) convertView.findViewById(android.R.id.text1))
                    .setText(getItem(position));
            return convertView;
        }


    }
}
