package aaronsoftech.in.nber.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import aaronsoftech.in.nber.App_Conteroller;
import aaronsoftech.in.nber.POJO.Response_register;
import aaronsoftech.in.nber.R;
import aaronsoftech.in.nber.Service.APIClient;
import aaronsoftech.in.nber.Utils.App_Utils;
import aaronsoftech.in.nber.Utils.SP_Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Vehicle_reg extends AppCompatActivity {
    TextView btn_ok;
    public static String PATH_PERMIT="",PATH_VEHICLE="",PATH_RC="",PATH_INSURENSE="",PATH_OTHER_DOC="";
    CircleImageView btn_permit,btn_vehicle,btn_vehicle_rc,btn_insurence_id,btn_other_doc;
    EditText ed_name,ed_seating,ed_no;
    ProgressDialog progressDialog;
    String TAG="Vehicle_reg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_reg);

        Init();

        btn_permit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Vehicle_reg.this,Driver_doc_Image.class).
                        putExtra("type","11"));           }
        });

        btn_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Vehicle_reg.this,Driver_doc_Image.class).
                        putExtra("type","12"));            }
        });

        btn_vehicle_rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Vehicle_reg.this,Driver_doc_Image.class).
                        putExtra("type","13"));            }
        });

        btn_insurence_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Vehicle_reg.this,Driver_doc_Image.class).
                        putExtra("type","14"));            }
        });

        btn_other_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Vehicle_reg.this,Driver_doc_Image.class).
                        putExtra("type","15"));            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PATH_PERMIT=="")
                {
                    Toast.makeText(Vehicle_reg.this, "Upload Permit", Toast.LENGTH_SHORT).show();
                }else if (PATH_VEHICLE=="")
                {
                    Toast.makeText(Vehicle_reg.this, "Upload Permit", Toast.LENGTH_SHORT).show();
                }else if (PATH_RC=="")
                {
                    Toast.makeText(Vehicle_reg.this, "Upload Permit", Toast.LENGTH_SHORT).show();
                }else if (PATH_INSURENSE=="")
                {
                    Toast.makeText(Vehicle_reg.this, "Upload Permit", Toast.LENGTH_SHORT).show();
                }else if (PATH_OTHER_DOC=="")
                {
                    Toast.makeText(Vehicle_reg.this, "Upload Permit", Toast.LENGTH_SHORT).show();
                }else if (ed_name.getText().toString().isEmpty())
                {
                    ed_name.setError("Enter name");
                    ed_name.requestFocus();
                }else if (ed_seating.getText().toString().isEmpty())
                {
                    ed_seating.setError("Enter seating capacity");
                    ed_seating.requestFocus();
                }else if (ed_no.getText().toString().isEmpty())
                {
                    ed_no.setError("Enter vehicle no.");
                    ed_no.requestFocus();
                }else{
                    Call_Api();
                }

            }
        });

    }

    private void Call_Api() {

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        String new_date=dateFormat.format(date);

        progressDialog=new ProgressDialog(Vehicle_reg.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        File file_permit = new File(PATH_PERMIT);
        File file_vehicle = new File(PATH_VEHICLE);
        File file_rc = new File(PATH_RC);
        File file_other_doc = new File(PATH_OTHER_DOC);
        File file_insurense = new File(PATH_INSURENSE);

        RequestBody request_file_permit = RequestBody.create(MediaType.parse("multipart/form-data"), file_permit);
        RequestBody request_file_vehicle = RequestBody.create(MediaType.parse("multipart/form-data"), file_vehicle);
        RequestBody request_file_rc = RequestBody.create(MediaType.parse("multipart/form-data"), file_rc);
        RequestBody request_file_other_doc = RequestBody.create(MediaType.parse("multipart/form-data"), file_other_doc);
        RequestBody request_file_insurense = RequestBody.create(MediaType.parse("multipart/form-data"), file_insurense);

        MultipartBody.Part body_request_permit = MultipartBody.Part.createFormData("permit", file_permit.getName(), request_file_permit);
        MultipartBody.Part body_request_vehicle = MultipartBody.Part.createFormData("vehicle_photo", file_vehicle.getName(), request_file_vehicle);
        MultipartBody.Part body_request_rc = MultipartBody.Part.createFormData("vehicle_rc", file_rc.getName(), request_file_rc);
        MultipartBody.Part body_request_other_doc = MultipartBody.Part.createFormData("vehicle_other_doc", file_other_doc.getName(), request_file_other_doc);
        MultipartBody.Part body_request_insurense = MultipartBody.Part.createFormData("vehicle_insurance_id", file_insurense.getName(), request_file_insurense);

        String userId= App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ID,"");
        RequestBody body_driver_id =RequestBody.create(okhttp3.MultipartBody.FORM, ""+userId);

        RequestBody body_name =RequestBody.create(okhttp3.MultipartBody.FORM, ""+ed_name.getText().toString().trim());
        RequestBody body_number =RequestBody.create(okhttp3.MultipartBody.FORM, ""+ed_no.getText().toString().trim());
        RequestBody body_seating =RequestBody.create(okhttp3.MultipartBody.FORM, ""+ed_seating.getText().toString().trim());

        RequestBody body_timestamp =RequestBody.create(okhttp3.MultipartBody.FORM, ""+new_date.toString());


        Call<Response_register> call= APIClient.getWebServiceMethod().vehicle_register(body_driver_id,body_name,body_number,
                body_timestamp,body_request_permit,body_request_vehicle,body_request_rc,body_request_other_doc,body_request_insurense);
        call.enqueue(new Callback<Response_register>() {
            @Override
            public void onResponse(Call<Response_register> call, Response<Response_register> response) {
                progressDialog.dismiss();
                Toast.makeText(Vehicle_reg.this, "success", Toast.LENGTH_SHORT).show();
                try{
                    Log.i(TAG,"response driver getid:  "+response.body().getId().toString());
                }catch (Exception e){e.printStackTrace();}
                try{Log.i(TAG,"response driver getApi_message:  "+response.body().getApi_message().toString());
                }catch (Exception e){e.printStackTrace();}
                try{Log.i(TAG,"response driver getApi_status:  "+response.body().getApi_status().toString());
                }catch (Exception e){e.printStackTrace();}
            }

            @Override
            public void onFailure(Call<Response_register> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Vehicle_reg.this, "Error: "+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void Init() {
        btn_ok=findViewById(R.id.btn_submit);
        btn_permit=findViewById(R.id.pic_permit);
        btn_other_doc=findViewById(R.id.pic_other);
        btn_vehicle=findViewById(R.id.pic_vehicle);
        btn_vehicle_rc=findViewById(R.id.pic_vehicle_rc);
        btn_insurence_id=findViewById(R.id.pic_insurece);
        ed_name=findViewById(R.id.txt_vehicle_name);
        ed_seating=findViewById(R.id.txt_vehicle_seating);
        ed_no=findViewById(R.id.vehicle_no);
    }

    @Override
    protected void onResume() {
        Set_docu_pic();
        super.onResume();
    }

    private void Set_docu_pic() {
        if (PATH_PERMIT!=""){ App_Utils.loadProfileImage(Vehicle_reg.this,PATH_PERMIT,btn_permit); }
        if (PATH_VEHICLE!=""){ App_Utils.loadProfileImage(Vehicle_reg.this,PATH_VEHICLE,btn_vehicle);  }
        if (PATH_RC!=""){ App_Utils.loadProfileImage(Vehicle_reg.this,PATH_RC,btn_vehicle_rc);  }
        if (PATH_INSURENSE!=""){ App_Utils.loadProfileImage(Vehicle_reg.this,PATH_INSURENSE,btn_insurence_id);  }
        if (PATH_OTHER_DOC!=""){ App_Utils.loadProfileImage(Vehicle_reg.this,PATH_OTHER_DOC,btn_other_doc); }

    }
}
