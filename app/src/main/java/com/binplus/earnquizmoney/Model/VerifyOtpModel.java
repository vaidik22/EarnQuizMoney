package com.binplus.earnquizmoney.Model;

public class VerifyOtpModel {
    public boolean response;
    public String message;
    public Data data;
    @Override
    public String toString() {
        return "VerifyOtpModel{" +
                "response=" + response +
                '}';
    }
    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        public String id;
        public String name;
        public String email;
        public Object age;
        public String mobile;
        public String wallet;
        public String address;
        public String state;
        public String district;
        public String qualification;
        public String type;
        public String profile;
        public Object facbook_link;
        public Object instagram_link;
        public Object twitter_link;
        public Object whatsapp;
        public Object googlepay_number;
        public Object phonepe_number;
        public Object paytm_number;
        public String name_on_bank;
        public String bank_name;
        public Object account_number;
        public String ifsc_code;
        public Object refer_code;
        public String own_code;
        public String status;
        public String imei;
        public String device_id;
        public String created_at;
        public String update_at;
        public String total_refer_count;
        public String total_played_game;
        public String total_amount_spend;
        public String total_amount_earned;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getAge() {
            return age;
        }

        public void setAge(Object age) {
            this.age = age;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getWallet() {
            return wallet;
        }

        public void setWallet(String wallet) {
            this.wallet = wallet;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getQualification() {
            return qualification;
        }

        public void setQualification(String qualification) {
            this.qualification = qualification;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public Object getFacbook_link() {
            return facbook_link;
        }

        public void setFacbook_link(Object facbook_link) {
            this.facbook_link = facbook_link;
        }

        public Object getInstagram_link() {
            return instagram_link;
        }

        public void setInstagram_link(Object instagram_link) {
            this.instagram_link = instagram_link;
        }

        public Object getTwitter_link() {
            return twitter_link;
        }

        public void setTwitter_link(Object twitter_link) {
            this.twitter_link = twitter_link;
        }

        public Object getWhatsapp() {
            return whatsapp;
        }

        public void setWhatsapp(Object whatsapp) {
            this.whatsapp = whatsapp;
        }

        public Object getGooglepay_number() {
            return googlepay_number;
        }

        public void setGooglepay_number(Object googlepay_number) {
            this.googlepay_number = googlepay_number;
        }

        public Object getPhonepe_number() {
            return phonepe_number;
        }

        public void setPhonepe_number(Object phonepe_number) {
            this.phonepe_number = phonepe_number;
        }

        public Object getPaytm_number() {
            return paytm_number;
        }

        public void setPaytm_number(Object paytm_number) {
            this.paytm_number = paytm_number;
        }

        public String getName_on_bank() {
            return name_on_bank;
        }

        public void setName_on_bank(String name_on_bank) {
            this.name_on_bank = name_on_bank;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public Object getAccount_number() {
            return account_number;
        }

        public void setAccount_number(Object account_number) {
            this.account_number = account_number;
        }

        public String getIfsc_code() {
            return ifsc_code;
        }

        public void setIfsc_code(String ifsc_code) {
            this.ifsc_code = ifsc_code;
        }

        public Object getRefer_code() {
            return refer_code;
        }

        public void setRefer_code(Object refer_code) {
            this.refer_code = refer_code;
        }

        public String getOwn_code() {
            return own_code;
        }

        public void setOwn_code(String own_code) {
            this.own_code = own_code;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdate_at() {
            return update_at;
        }

        public void setUpdate_at(String update_at) {
            this.update_at = update_at;
        }

        public String getTotal_refer_count() {
            return total_refer_count;
        }

        public void setTotal_refer_count(String total_refer_count) {
            this.total_refer_count = total_refer_count;
        }

        public String getTotal_played_game() {
            return total_played_game;
        }

        public void setTotal_played_game(String total_played_game) {
            this.total_played_game = total_played_game;
        }

        public String getTotal_amount_spend() {
            return total_amount_spend;
        }

        public void setTotal_amount_spend(String total_amount_spend) {
            this.total_amount_spend = total_amount_spend;
        }

        public String getTotal_amount_earned() {
            return total_amount_earned;
        }

        public void setTotal_amount_earned(String total_amount_earned) {
            this.total_amount_earned = total_amount_earned;
        }
    }
}
