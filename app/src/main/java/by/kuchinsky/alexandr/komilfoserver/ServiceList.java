package by.kuchinsky.alexandr.komilfoserver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import by.kuchinsky.alexandr.komilfoserver.Common.Common;
import by.kuchinsky.alexandr.komilfoserver.Interface.ItemClickListener;
import by.kuchinsky.alexandr.komilfoserver.Model.Category;
import by.kuchinsky.alexandr.komilfoserver.Model.Service;
import by.kuchinsky.alexandr.komilfoserver.ViewHolder.ServiceViewHolder;
import info.hoang8f.widget.FButton;

public class ServiceList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    RelativeLayout rootLayout;

    FloatingActionButton fab;

    //firebase

    FirebaseDatabase db;
    DatabaseReference servicelist;
    FirebaseStorage storage;
    StorageReference storageReference;

    String categoryId="";
    FirebaseRecyclerAdapter<Service, ServiceViewHolder> adapter;


    MaterialEditText edtName, edtDescription, edtPrice, edtDiscount;
    FButton btnSelect, btnUpload;

    Service newService;

    Uri saveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        //firebase

        db= FirebaseDatabase.getInstance();
        servicelist = db.getReference("Service");
        storage= FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //init

        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_service);
        recyclerView.setHasFixedSize(true);
        layoutManager =  new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fab = (FloatingActionButton)findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showAddDialog();
            }
        });
        if (getIntent() != null){
            categoryId = getIntent().getStringExtra("CategoryId");
            if (!categoryId.isEmpty()){
                loadListFood(categoryId);
            }
        }

    }

    private void showAddDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ServiceList.this);
        alertDialog.setTitle("Добавить новый сервис:");
        alertDialog.setMessage("Пожалуйста, введите полную информацию.");


        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_service,null);

        edtName= add_menu_layout.findViewById(R.id.edtName);
        edtDescription= add_menu_layout.findViewById(R.id.edtDescription);
        edtPrice= add_menu_layout.findViewById(R.id.edtPrice);
        edtDiscount= add_menu_layout.findViewById(R.id.edtDiscount);


        btnSelect=add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload=add_menu_layout.findViewById(R.id.btnUpload);

        //event for butons

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(); //select user picture from phone and save uri this image
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_status);

        //Set button
        alertDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //just create a new service
                if (newService != null){
                    servicelist.push().setValue(newService);

                    Snackbar.make(rootLayout, "Сервис: "+newService.getName()+" добавлен! ", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();


    }

    private void uploadImage() {

        if (saveUri != null){
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Загружаем..");
            pd.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();
                    Toast.makeText(ServiceList.this, "Загружено", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //set new znachenie foe new category if image upload succ and we can get link for downloading
                            newService = new Service();
                            newService.setName(edtName.getText().toString());
                            newService.setDescription(edtDescription.getText().toString());
                            newService.setPrice(edtPrice.getText().toString());
                            newService.setDiscount(edtDiscount.getText().toString());
                            newService.setMenuId(categoryId);
                            newService.setImage(uri.toString());

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(ServiceList.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() /
                            taskSnapshot.getTotalByteCount());
                    pd.setMessage("Зaгружено: "+progress+"%");
                }
            });

        }




    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), Common.PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null){
            saveUri = data.getData();
            btnSelect.setText("Изображение выбрано!");
        }


    }

    private void loadListFood(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Service, ServiceViewHolder>(
                Service.class,
                R.layout.service_item,
                ServiceViewHolder.class,
                servicelist.orderByChild("menuId").equalTo(categoryId)
        ) {
            @Override
            protected void populateViewHolder(ServiceViewHolder viewHolder,
                                              Service model, int position) {
                    viewHolder.service_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.service_image);
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //late
                    }
                });
            }
        };
adapter.notifyDataSetChanged();
recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(Common.UPDATE)){
            showUpdateServiceDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));

        } else if (item.getTitle().equals(Common.DELETE)){
            deleteService(adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);

    }

    private void deleteService(String key) {
        servicelist.child(key).removeValue();
    }

    private void showUpdateServiceDialog(final String key, final Service item) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ServiceList.this);
        alertDialog.setTitle("Редактирование");
        alertDialog.setMessage("Пожалуйста, введите полную информацию.");


        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_service,null);

        edtName= add_menu_layout.findViewById(R.id.edtName);
        edtDescription= add_menu_layout.findViewById(R.id.edtDescription);
        edtPrice= add_menu_layout.findViewById(R.id.edtPrice);
        edtDiscount= add_menu_layout.findViewById(R.id.edtDiscount);

        //set a default item for the service
        edtName.setText(item.getName());
        edtDescription.setText(item.getDescription());
        edtPrice.setText(item.getPrice());
        edtDiscount.setText(item.getDiscount());


        btnSelect=add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload=add_menu_layout.findViewById(R.id.btnUpload);

        //event for butons

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_status);

        //Set button
        alertDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //just create a new service
             //   if (newService != null){

                    item.setName(edtName.getText().toString());
                    item.setPrice(edtPrice.getText().toString());
                    item.setDiscount(edtDiscount.getText().toString());
                    item.setDescription(edtDescription.getText().toString());

                    servicelist.child(key).setValue(item);

                    Snackbar.make(rootLayout, "Сервис: "+newService.getName()+" изменён! ", Snackbar.LENGTH_SHORT).show();
              //  }
            }
        });
        alertDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();


    }

    private void changeImage(final Service item) {



        if (saveUri != null){
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Загружаем..");
            pd.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();
                    Toast.makeText(ServiceList.this, "Загружено", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //set new znachenie foe new category if image upload succ and we can get link for downloading

                            item.setImage(uri.toString());
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(ServiceList.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() /
                            taskSnapshot.getTotalByteCount());
                    pd.setMessage("Зaгружено: "+progress+"%");
                }
            });






        }
    }
}
