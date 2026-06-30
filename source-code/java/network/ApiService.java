package com.example.uas_webservice.network;

import com.example.uas_webservice.model.Mahasiswa;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("mahasiswa.php")
    Call<List<Mahasiswa>> getAllMahasiswa();

    @FormUrlEncoded
    @POST("insert.php")
    Call<ResponseBody> insertMahasiswa(
            @Field("NIM")     String nim,
            @Field("Nama")    String nama,
            @Field("Jurusan") String jurusan,
            @Field("Alamat")  String alamat
    );

    @FormUrlEncoded
    @POST("update.php")
    Call<ResponseBody> updateMahasiswa(
            @Field("NIM")     String nim,
            @Field("Nama")    String nama,
            @Field("Jurusan") String jurusan,
            @Field("Alamat")  String alamat
    );

    @FormUrlEncoded
    @POST("delete.php")
    Call<ResponseBody> deleteMahasiswa(
            @Field("NIM") String nim
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> login(
            @Field("username") String username,
            @Field("password") String password
    );
}
