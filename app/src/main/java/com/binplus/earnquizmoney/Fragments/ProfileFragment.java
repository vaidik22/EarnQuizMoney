package com.binplus.earnquizmoney.Fragments;

import static com.binplus.earnquizmoney.BaseURL.BaseURL.BASE_URL_IMAGE;
import static com.binplus.earnquizmoney.BaseURL.BaseURL.upload_profile_image;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.binplus.earnquizmoney.Model.ProfileModel;
import com.binplus.earnquizmoney.Model.UpdateProfileImageModel;
import com.binplus.earnquizmoney.Model.UpdateProfileModel;
import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.retrofit.Api;
import com.binplus.earnquizmoney.retrofit.RetrofitClient;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    LinearLayout layout_basic,layout_basic_edit,layout_social,layout_social_media,layout_refer,layout_basic_refer;
    ImageView img_down_basic,img_down_social,img_down_refer;
    FrameLayout editProfileImg;
    CircleImageView iv_cir;
    ArrayList<ProfileModel.Data> profileList = new ArrayList<>();
    ArrayList<UpdateProfileModel.Data> updatedProfileList = new ArrayList<>();
    Api apiInterface;
    TextView tv_name,tv_user_mobile,tv_total_played,tv_total_spent,tv_total_earned;
    EditText username,mobile_number,email,age,state,district,address,et_facebook,whatsaap,et_refer;
    Button btn_submit_basic,btn_submit_social,btn_submit_refer;
    String base64Image;
    UpdateProfileModel updateProfileModel;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private static final int PICK_IMAGE_REQUEST = 1;
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = RetrofitClient.getRetrofitInstance().create(Api.class);
         updateProfileModel = new UpdateProfileModel();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initview(view);
        fetchProfileDetails();
        allClick();
        return  view;
    }

    private void allClick() {
        layout_basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout_basic_edit.getVisibility() == View.VISIBLE) {
                    layout_basic_edit.setVisibility(View.GONE);
                    img_down_basic.animate().rotation(0).setDuration(300).start();
                } else {
                    layout_basic_edit.setVisibility(View.VISIBLE);
                    img_down_basic.animate().rotation(180).setDuration(300).start();
                }
            }
        });
        layout_social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout_social_media.getVisibility() == View.VISIBLE){
                    layout_social_media.setVisibility(View.GONE);
                    img_down_social.animate().rotation(0).setDuration(300).start();
                }else {
                    layout_social_media.setVisibility(View.VISIBLE);
                    img_down_social.animate().rotation(180).setDuration(300).start();
                }
            }
        });
        layout_refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout_basic_refer.getVisibility() == View.VISIBLE){
                    layout_basic_refer.setVisibility(View.GONE);
                    img_down_refer.animate().rotation(0).setDuration(300).start();
                }else {
                    layout_basic_refer.setVisibility(View.VISIBLE);
                    img_down_refer.animate().rotation(180).setDuration(300).start();
                }
            }
        });
        editProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImagePicker();
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                }
            }
        });
        btn_submit_basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(base64Image);
            }
        });
    }

    private void openImagePicker() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { cameraIntent });

        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
        } else {
            // If no camera app is available, show the gallery picker
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                if (data != null) {
                    if (data.getData() != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), data.getData());
                            iv_cir.setImageBitmap(bitmap);
                            base64Image = convertBitmapToBase64(bitmap);
                            updateProfile(base64Image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (data.getExtras() != null) {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        iv_cir.setImageBitmap(bitmap);
                        String base64Image = convertBitmapToBase64(bitmap);
                        updateProfile(base64Image);
                    }
                }
            }
        }
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

            Log.d("Base64Image", "Base64 Image String Length: " + base64Image.length());

            return base64Image;
        } catch (Exception e) {
            Log.e("Base64Error", "Error converting bitmap to Base64", e);
            return null;
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (Exception e) {
                Log.e("StreamCloseError", "Error closing ByteArrayOutputStream", e);
            }
        }
    }


    private void fetchProfileDetails() {
        profileList.clear();
        JsonObject postData = new JsonObject();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String authId = sharedPreferences.getString("userId", "Default Id");
        postData.addProperty("user_id", authId);

        Call<ProfileModel> call = apiInterface.getProfileApi(postData);
        call.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(@NonNull Call<ProfileModel> call, @NonNull Response<ProfileModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                        profileList.add(response.body().getData());
                    updateUI(profileList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileModel> call, @NonNull Throwable t) {
                // Handle failure
            }
        });
    }
    private void updateProfile(String imageUri) {
        profileList.clear();
        JsonObject postData = new JsonObject();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String authId = sharedPreferences.getString("userId", "Default Id");
        postData.addProperty("update_profile", "1");
        postData.addProperty("user_id", authId);
        postData.addProperty("profile", imageUri);
        postData.addProperty("name", username.getText().toString());
        postData.addProperty("email", email.getText().toString());
        postData.addProperty("mobile", mobile_number.getText().toString());


        Call<UpdateProfileModel> call = apiInterface.getUpdateProfileApi(postData);
        call.enqueue(new Callback<UpdateProfileModel>() {
            @Override
            public void onResponse(@NonNull Call<UpdateProfileModel> call, @NonNull Response<UpdateProfileModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UpdateProfileModel updateProfileModel = response.body();
                    String message = updateProfileModel.getMessage();
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    updatedProfileList.add(updateProfileModel.getData());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateProfileModel> call, @NonNull Throwable t) {
                // Handle failure
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void updateUI(ArrayList<ProfileModel.Data> profile) {
        String imageUrl = BASE_URL_IMAGE + profile.get(0).getProfile();
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(iv_cir);
        tv_name.setText(profile.get(0).getName());
        tv_user_mobile.setText(profile.get(0).getMobile());
        tv_total_played.setText(profile.get(0).getTotal_played_game());
        tv_total_spent.setText("Rs."+profile.get(0).getTotal_amount_spend());
        tv_total_earned.setText("Rs."+profile.get(0).getTotal_amount_earned());
        username.setText(profile.get(0).getName());
        mobile_number.setText(profile.get(0).getMobile());
        email.setText(profile.get(0).getEmail());
        age.setText(profile.get(0).getAge());
        state.setText(profile.get(0).getState());
        district.setText(profile.get(0).getDistrict());
        address.setText(profile.get(0).getAddress());
        et_refer.setText(profile.get(0).getRefer_code());
    }


    private void initview(View view) {
        layout_basic = view.findViewById(R.id.layout_basic);
        layout_basic_edit = view.findViewById(R.id.layout_basic_edit);
        img_down_basic = view.findViewById(R.id.img_down_basic);
        layout_social = view.findViewById(R.id.layout_social);
        layout_social_media = view.findViewById(R.id.layout_social_media);
        img_down_social = view.findViewById(R.id.img_down_social);
        layout_refer = view.findViewById(R.id.layout_refer);
        layout_basic_refer = view.findViewById(R.id.layout_basic_refer);
        img_down_refer = view.findViewById(R.id.img_down_refer);
        editProfileImg = view.findViewById(R.id.edit_profile_img);
        iv_cir = view.findViewById(R.id.iv_cir);
        tv_name = view.findViewById(R.id.tv_name);
        tv_user_mobile = view.findViewById(R.id.tv_user_mobile);
        tv_total_played = view.findViewById(R.id.tv_total_played);
        tv_total_spent = view.findViewById(R.id.tv_total_spent);
        tv_total_earned = view.findViewById(R.id.tv_total_earned);
        username = view.findViewById(R.id.username);
        mobile_number = view.findViewById(R.id.mobile_number);
        email = view.findViewById(R.id.email);
        age = view.findViewById(R.id.age);
        state = view.findViewById(R.id.state);
        district = view.findViewById(R.id.district);
        address = view.findViewById(R.id.address);
        et_facebook = view.findViewById(R.id.et_facebook);
        whatsaap = view.findViewById(R.id.whatsaap);
        et_refer = view.findViewById(R.id.et_refer);
        btn_submit_basic = view.findViewById(R.id.btn_submit_basic);
    }
}