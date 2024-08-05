package com.binplus.earnquizmoney.retrofit;

public class ConfigModel {
    public boolean response;
    public String message;
    public Data data;

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
        public String play_store_link;
        public String app_link;
        public String share_msg;
        public String share_link;
        public String min_add_amount;
        public String version;
        public String upi_id;
        public String withdrawal_coin_max;
        public String withdrawal_coin_min;
        public String mobile_msg_status;
        public String email_msg_status;
        public String support_number;
        public String share_message;
        public String facebook_link;
        public String instagram_link;
        public String twitter_link;
        public String add_money_value;
        public String referral_point;
        public String is_force;
        public String razorpay_id;
        public String merchat_key;
        public String upi_status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlay_store_link() {
            return play_store_link;
        }

        public void setPlay_store_link(String play_store_link) {
            this.play_store_link = play_store_link;
        }

        public String getApp_link() {
            return app_link;
        }

        public void setApp_link(String app_link) {
            this.app_link = app_link;
        }

        public String getShare_msg() {
            return share_msg;
        }

        public void setShare_msg(String share_msg) {
            this.share_msg = share_msg;
        }

        public String getShare_link() {
            return share_link;
        }

        public void setShare_link(String share_link) {
            this.share_link = share_link;
        }

        public String getMin_add_amount() {
            return min_add_amount;
        }

        public void setMin_add_amount(String min_add_amount) {
            this.min_add_amount = min_add_amount;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getUpi_id() {
            return upi_id;
        }

        public void setUpi_id(String upi_id) {
            this.upi_id = upi_id;
        }

        public String getWithdrawal_coin_max() {
            return withdrawal_coin_max;
        }

        public void setWithdrawal_coin_max(String withdrawal_coin_max) {
            this.withdrawal_coin_max = withdrawal_coin_max;
        }

        public String getWithdrawal_coin_min() {
            return withdrawal_coin_min;
        }

        public void setWithdrawal_coin_min(String withdrawal_coin_min) {
            this.withdrawal_coin_min = withdrawal_coin_min;
        }

        public String getMobile_msg_status() {
            return mobile_msg_status;
        }

        public void setMobile_msg_status(String mobile_msg_status) {
            this.mobile_msg_status = mobile_msg_status;
        }

        public String getEmail_msg_status() {
            return email_msg_status;
        }

        public void setEmail_msg_status(String email_msg_status) {
            this.email_msg_status = email_msg_status;
        }

        public String getSupport_number() {
            return support_number;
        }

        public void setSupport_number(String support_number) {
            this.support_number = support_number;
        }

        public String getShare_message() {
            return share_message;
        }

        public void setShare_message(String share_message) {
            this.share_message = share_message;
        }

        public String getFacebook_link() {
            return facebook_link;
        }

        public void setFacebook_link(String facebook_link) {
            this.facebook_link = facebook_link;
        }

        public String getInstagram_link() {
            return instagram_link;
        }

        public void setInstagram_link(String instagram_link) {
            this.instagram_link = instagram_link;
        }

        public String getTwitter_link() {
            return twitter_link;
        }

        public void setTwitter_link(String twitter_link) {
            this.twitter_link = twitter_link;
        }

        public String getAdd_money_value() {
            return add_money_value;
        }

        public void setAdd_money_value(String add_money_value) {
            this.add_money_value = add_money_value;
        }

        public String getReferral_point() {
            return referral_point;
        }

        public void setReferral_point(String referral_point) {
            this.referral_point = referral_point;
        }

        public String getIs_force() {
            return is_force;
        }

        public void setIs_force(String is_force) {
            this.is_force = is_force;
        }

        public String getRazorpay_id() {
            return razorpay_id;
        }

        public void setRazorpay_id(String razorpay_id) {
            this.razorpay_id = razorpay_id;
        }

        public String getMerchat_key() {
            return merchat_key;
        }

        public void setMerchat_key(String merchat_key) {
            this.merchat_key = merchat_key;
        }

        public String getUpi_status() {
            return upi_status;
        }

        public void setUpi_status(String upi_status) {
            this.upi_status = upi_status;
        }
    }
}
