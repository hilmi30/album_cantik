package com.kartinimedia.albumcantik.network

import com.kartinimedia.albumcantik.model.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*
import retrofit2.http.POST
import retrofit2.http.Multipart



interface ApiRepo {
    @FormUrlEncoded
    @POST("auth/signup")
    fun signup(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("full_name") fullName: String,
        @Field("gender_id") genderId: Int
    ): Observable<SignupModel>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Observable<LoginModel>

    @FormUrlEncoded
    @POST("auth/update-my-profile")
    fun updateProfile(
        @Query("access-token") accessToken: String,
        @Field("gender_id") genderId: Int?,
        @Field("full_name") fullName: String?,
        @Field("password_lama") passwordLama: String?,
        @Field("password_baru") passwordBaru: String?,
        @Field("password_repeat") passwordRepeat: String?,
        @Field("alamat_id") alamatId: Int?
    ): Observable<UpdateProfileModel>

    @FormUrlEncoded
    @POST("auth/resend-verification-email")
    fun resendEmail(
        @Field("email") email: String
    ): Observable<ResendEmailModel>

    @FormUrlEncoded
    @POST("auth/forgot-password")
    fun lupaPass(
        @Field("email") email: String
    ): Observable<ForgotPassModel>

    @GET("gender/gender")
    fun getGender(): Observable<GetGenderModel>

    @GET("category/category")
    fun getAllCategory(
        @Query("page") page: Int
    ): Observable<AllCategoryModel>

    @POST("product/photobook")
    fun getProduct(
        @Query("ProductSearch[kategori_produk_id]") kategoriId: Int,
        @Query("page") page: Int
    ): Observable<ProductModel>

    @POST("product/photobook")
    fun getProductById(
        @Query("ProductSearch[id]") productId: Int
    ): Observable<ProductModel>

    @Multipart
    @POST("upload/upload2")
    fun uploadPhoto(
        @Part file: MultipartBody.Part,
        @Query("access-token") accessToken: String,
        @Part("product_id") productId: Int,
        @Part("number") number: Int
    ): Observable<UploadModel>

    @GET("upload/get-product-draft")
    fun getAllDraft(
        @Query("access-token") accessToken: String
    ): Observable<DraftModel>

    @GET("upload/get-draft-by-product")
    fun getDraftByProductId(
        @Query("access-token") accessToken: String,
        @Query("product_id") productId: Int
    ): Observable<DraftByProductIdModel>

    @GET("alamat")
    fun getAlamat(
        @Query("access-token") accessToken: String
    ): Observable<AlamatListModel>

    @POST("wilayah/provinsi")
    fun getProvinsi(): Observable<LokasiModel>

    @POST("wilayah/kabupaten")
    fun getKabupaten(
        @Query("KabupatenSearch[id_prov]") idProv: Int
    ): Observable<LokasiModel>

    @POST("wilayah/kecamatan")
    fun getKecamatan(
        @Query("KecamatanSearch[id_kab]") idKab: Int
    ): Observable<LokasiModel>

    @FormUrlEncoded
    @POST("alamat/create")
    fun addAlamat(
        @Query("access-token") accessToken: String,
        @Field("alamat") alamat: String,
        @Field("atas_nama") atasNama: String,
        @Field("nomer_hp") nomerHp: String,
        @Field("id_prov") idProv: Int,
        @Field("id_kab") idKab: Int,
        @Field("id_kec") idKec: Int,
        @Field("kode_pos") kodePos: String
    ): Observable<AlamatModel>

    @FormUrlEncoded
    @POST("alamat/update")
    fun updateAlamat(
        @Query("access-token") accessToken: String,
        @Query("id") alamatId: Int,
        @Field("alamat") alamat: String,
        @Field("atas_nama") atasNama: String,
        @Field("nomer_hp") nomerHp: String,
        @Field("id_prov") idProv: Int?,
        @Field("id_kab") idKab: Int?,
        @Field("id_kec") idKec: Int?,
        @Field("kode_pos") kodePos: String
    ): Observable<AlamatModel>

    @POST("alamat/delete")
    fun deleteAlamat(
        @Query("access-token") accessToken: String,
        @Query("id") alamatId: Int
    ): Observable<AlamatListModel>

    @FormUrlEncoded
    @POST("order/create")
    fun addOrder(
        @Query("access-token") accessToken: String,
        @Field("jumlah_order") jumlahOrder: Int,
        @Field("payment_id") paymentId: Int,
        @Field("order_name") orderName: String,
        @Field("order_email") orderEmail: String,
        @Field("order_phone") orderPhone: String,
        @Field("order_address") orderAddress: String,
        @Field("prov_id") provId: Int,
        @Field("kab_id") kabId: Int,
        @Field("kec_id") kecId: Int,
        @Field("product_id") productId: Int,
        @Field("kode_pos") kodePos: Int,
        @Field("order_note") orderNote: String,
        @Field("kode_unik") kodeUnik: Int
    ): Observable<OrderModel>

    @GET("alamat")
    fun detailAlamat(
        @Query("access-token") accessToken: String,
        @Query("AlamatSearch[id]") alamatSearch: Int
    ): Observable<AlamatListModel>

    @POST("payment/payment")
    fun getPayment(
        @Query("PaymentSearch[parent_id]") id: Int
    ): Observable<PaymentModel>

    @FormUrlEncoded
    @POST("upload/hapus-draft")
    fun hapusDraft(
        @Query("access-token") accessToken: String,
        @Field("product_id") productId: Int
    ): Observable<DraftModel>

    @POST("order/order")
    fun listOrder(
        @Query("access-token") accessToken: String,
        @Query("page") page: Int
    ): Observable<ListOrderModel>

    @Multipart
    @POST("konfirmasi-pembayaran/create")
    fun konfirmasiPembayaran(
        @Query("access-token") accessToken: String,
        @Part("tbl_order_id") orderId: Int,
        @Part file: MultipartBody.Part
    ): Observable<PembayaranModel>

    @POST("order/delete")
    fun deleteOrder(
        @Query("access-token") accessToken: String,
        @Query("id") id: Int
    ): Observable<OrderModel>

    @GET("slider/slider")
    fun getPromo(): Observable<PromoModel>
}