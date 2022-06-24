package com.mahyco.cmr_app.leegality;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.mahyco.cmr_app.core.BaseActivity;
import com.mahyco.cmr_app.core.DLog;
import com.mahyco.cmr_app.leegality.Response.LeegalityMainResponse;
import com.mahyco.cmr_app.model.vendordetailsforcontract.VendorRecord;
import com.mahyco.cmr_app.view.DCESignActivity;
import com.mahyco.isp.core.MainApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LeegalityTaskHelper {

    //public static LeegalityTaskHelper instance;
    BaseActivity context;
    private static final int REQUEST_CODE = 1;
    ILeegalityHelper iLeegalityHelper;
    DCOnFieldInputs inputsDC;
    VendorRecord vendorRecord;

    public LeegalityTaskHelper(BaseActivity context,ILeegalityHelper iLeegalityHelper) {
        this.context = context;
        this.iLeegalityHelper = iLeegalityHelper;
    }
/*public static LeegalityTaskHelper getInstance(){
        if(instance==null){
            instance = new LeegalityTaskHelper();
        }
        return instance;
    }*/

 /* public  void setData(BaseActivity context){
        this.context = context;
    }*/

  public void doWorkContract(DCOnFieldInputs dcOnFieldInputs) {
        inputsDC = dcOnFieldInputs;
        vendorRecord = MainApplication.Companion.applicationContext().getVendorRecord();

        Log.d("BIND_DATA","INPUT FROM USER :: "+inputsDC.toString());
        Log.d("BIND_DATA","INPUT FROM API :: "+vendorRecord.toString());

      Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        new UploadDcumentTask().execute();
                    }
                });
                try {

                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    class UploadDcumentTask extends AsyncTask<String, Void, String> {

        private Context mContext;
        private Exception exception;

        protected String doInBackground(String... urls) {
            return   getRequestJson();
        }

        protected void onPostExecute(String result) {
            // TODO: check this.exception
            // TODO: do something with the feed
            if(result!=null){

                //set the data in uploadDataResponse model
                try {
                    if(!result.isEmpty()) {
                        JSONObject object = new JSONObject(result);
                        Gson gson = new Gson();
                        LeegalityMainResponse lMain = gson.fromJson(result, LeegalityMainResponse.class);
                         MainApplication.Companion.applicationContext().setMainResponse(lMain);

                        Log.d("LEEGALITY RESPONSE","POJO :::::::::::: "+lMain.toString());

                        /*JSONArray jsonArray =   new JSONArray( (new JSONObject(object.getString("data"))).getString("invitees"));
                        JSONObject object1 = new JSONObject((jsonArray.get(0).toString()));
                        Intent intent = new Intent("com.gspl.leegalityhelper.Leegality");
                        intent.putExtra("url", object1.getString("signUrl"));
                        context.startActivityForResult(intent, REQUEST_CODE);*/

                        Intent intent = new Intent(context, DCESignActivity.class);
                        context.startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getRequestJson() {

        RequestSign requestSign = new RequestSign();
        List<Invitee> invitees = new ArrayList<Invitee>();
        AadhaarConfig config = new AadhaarConfig();
        DscConfig config1 = new DscConfig();

        List<Field> fields = new ArrayList<Field>();

        /*fields.add(new Field());
        fields.get(0).setId("1604657728805");
        fields.get(0).setName("date");
        fields.get(0).setRequired(false);
        fields.get(0).setType("text");
        fields.get(0).setValue("24Jan2020");
        *//*---------------------------------------*//*
        fields.add(new Field());
        fields.get(1).setId("1604657747596");
        fields.get(1).setName("name");
        fields.get(1).setRequired(false);
        fields.get(1).setType("text");
        fields.get(1).setValue("Rajshri");
        *//*-----------------------------------------*//*
        fields.add(new Field());
        fields.get(2).setId("1604657766185");
        fields.get(2).setName("char");
        fields.get(2).setRequired(false);
        fields.get(2).setType("text");
        fields.get(2).setValue("Nice");*/

        /*<<<<<<<<<<<<<<<<<<<<<<<<<====================Field Entry START*====================>>>>>>>>>>>>>>>>>>>/

        /*0 Field Name : Name*/
        fields.add(new Field());
        fields.get(0).setId("1608532027647");
        fields.get(0).setName("name");
        fields.get(0).setRequired(true);
        fields.get(0).setType("text");
        fields.get(0).setValue(vendorRecord.getGrowerName());

        /*1 Field Name : Grower Son Of Name*/
        fields.add(new Field());
        fields.get(1).setId("1608532063408");
        fields.get(1).setName("so_name");
        fields.get(1).setRequired(false);
        fields.get(1).setType("text");
        fields.get(1).setValue(vendorRecord.getFatherName());

        /*2 Field Name : Grower Resident of Name*/
        fields.add(new Field());
        fields.get(2).setId("1608532108035");
        fields.get(2).setName("ro_name");
        fields.get(2).setRequired(false);
        fields.get(2).setType("text");
        fields.get(2).setValue(vendorRecord.getVillageName());

        /*3 Field Name : crop_name*/
        fields.add(new Field());
        fields.get(3).setId("1608532151023");
        fields.get(3).setName("crop_name");
        fields.get(3).setRequired(false);
        fields.get(3).setType("text");
        fields.get(3).setValue(vendorRecord.getCropDesc());

        /*4 Field Name : season*/
        fields.add(new Field());
        fields.get(4).setId("1608535056254");
        fields.get(4).setName("season");
        fields.get(4).setRequired(false);
        fields.get(4).setType("text");
        fields.get(4).setValue(vendorRecord.getSeason());

        /*5 Field Name : Year*/
        fields.add(new Field());
        fields.get(5).setId("1608532168354");
        fields.get(5).setName("year");
        fields.get(5).setRequired(false);
        fields.get(5).setType("text");
        //fields.get(5).setValue(""+Calendar.getInstance().get(Calendar.YEAR));
        fields.get(5).setValue(""+vendorRecord.getPrdYear());

        /*6 Field Name : acre_of_land*/
        fields.add(new Field());
        fields.get(6).setId("1608532219887");
        fields.get(6).setName("acre_of_land");
        fields.get(6).setRequired(false);
        fields.get(6).setType("text");
        fields.get(6).setValue(vendorRecord.getBprdAcre()); //TODO check

        /*7 Field Name : location*/
        fields.add(new Field());
        fields.get(7).setId("1608532376520");
        fields.get(7).setName("location");
        fields.get(7).setRequired(false);
        fields.get(7).setType("text");
        fields.get(7).setValue(vendorRecord.getVillageName()); //ToDo location details

        /*8 Field Name : plant_acre*/
        fields.add(new Field());
        fields.get(8).setId("1608532433848");
        fields.get(8).setName("plant_acre");
        fields.get(8).setRequired(false);
        fields.get(8).setType("text");
        fields.get(8).setValue(""); //TODO

        /*9 Field Name : program_fees*/
        fields.add(new Field());
        fields.get(9).setId("1608532468927");
        fields.get(9).setName("program_fees");
        fields.get(9).setRequired(false);
        fields.get(9).setType("text");
        fields.get(9).setValue(vendorRecord.getRegFees()); //TODO confirm

        /*10 Field Name : program_fees_in_words*/
        fields.add(new Field());
        fields.get(10).setId("1608532523650");
        fields.get(10).setName("program_fees_in_words");
        fields.get(10).setRequired(false);
        fields.get(10).setType("text");
        fields.get(10).setValue(vendorRecord.getRegFeeWord()); //TODO confirm

        /*11 Field Name : fees_validity*/
        fields.add(new Field());
        fields.get(11).setId("1608532606600");
        fields.get(11).setName("fees_validity");
        fields.get(11).setRequired(false);
        fields.get(11).setType("text");
        fields.get(11).setValue(""); //TODO

        /*12 Field Name : grower_name*/
        fields.add(new Field());
        fields.get(12).setId("1608537341940");
        fields.get(12).setName("grower_name");
        fields.get(12).setRequired(false);
        fields.get(12).setType("text");
        fields.get(12).setValue(vendorRecord.getGrowerName()); //TODO confirm

        /*13 Field Name : agreement_day*/
        fields.add(new Field());
        fields.get(13).setId("1608532762731");
        fields.get(13).setName("agreement_day");
        fields.get(13).setRequired(false);
        fields.get(13).setType("text");
        fields.get(13).setValue(""+Calendar.getInstance().get(Calendar.DAY_OF_YEAR));

        /*14 Field Name : agreement_month_year*/
        fields.add(new Field());
        fields.get(14).setId("1608532816477");
        fields.get(14).setName("agreement_month_year");
        fields.get(14).setRequired(false);
        fields.get(14).setType("text");
        fields.get(14).setValue(""+Calendar.getInstance().get(Calendar.YEAR));

        /*15 Field Name : grower_name*/
        fields.add(new Field());
        fields.get(15).setId("1608543038410");
        fields.get(15).setName("grower_name");
        fields.get(15).setRequired(false);
        fields.get(15).setType("text");
        fields.get(15).setValue(vendorRecord.getGrowerName()); //TODO confirm

        /*16 Field Name : agreement_reader_name*/
        fields.add(new Field());
        fields.get(16).setId("1608532900428");
        fields.get(16).setName("agreement_reader_name");
        fields.get(16).setRequired(false);
        fields.get(16).setType("text");
        fields.get(16).setValue(inputsDC.firstPartyFullName); //TODO confirm

        /*17 Field Name : thumb_impression_of_grower*/
        /*fields.add(new Field());
        fields.get(17).setId("1608533798897");
        fields.get(17).setName("thumb_impression_of_grower");
        fields.get(17).setRequired(false);
        fields.get(17).setType("text");
        fields.get(17).setValue("");*/ /*Remark Commented on 27 Jan 2021*/

        /*17 Field Name : name_of_translator*/
        fields.add(new Field());
        fields.get(17).setId("1608533940411");
        fields.get(17).setName("name_of_translator");
        fields.get(17).setRequired(false);
        fields.get(17).setType("text");
        fields.get(17).setValue(inputsDC.firstPartyFullName); //TODO confirm

        /*18 Field Name : witness_name*/
        fields.add(new Field());
        fields.get(18).setId("1608533982573");
        fields.get(18).setName("witness_name");
        fields.get(18).setRequired(false);
        fields.get(18).setType("text");
        fields.get(18).setValue(inputsDC.witnessOneName);

        /*19 Field Name : witness_name*/
        fields.add(new Field());
        fields.get(19).setId("1608533982573");
        fields.get(19).setName("witness_name");
        fields.get(19).setRequired(false);
        fields.get(19).setType("text");
        fields.get(19).setValue(inputsDC.witnessOneName);

        /*20 Field Name : order_no*/
        fields.add(new Field());
        fields.get(20).setId("1608534232606");
        fields.get(20).setName("order_no");
        fields.get(20).setRequired(false);
        fields.get(20).setType("text");
        fields.get(20).setValue(vendorRecord.getOrderno());

        /*21 Field Name : code_number*/
        fields.add(new Field());
        fields.get(21).setId("1608534271406");
        fields.get(21).setName("code_number");
        fields.get(21).setRequired(false);
        fields.get(21).setType("text");
        fields.get(21).setValue(""); //TODO Confirm

        /*22 Field Name : agreement_for*/
        fields.add(new Field());
        fields.get(22).setId("1608534406906");
        fields.get(22).setName("agreement_for");
        fields.get(22).setRequired(false);
        fields.get(22).setType("text");
        fields.get(22).setValue(vendorRecord.getCropDesc()); //TODO Confirm

        /*23 Field Name : agreement_day*/
        fields.add(new Field());
        fields.get(23).setId("1608534469021");
        fields.get(23).setName("agreement_day");
        fields.get(23).setRequired(false);
        fields.get(23).setType("text");
        fields.get(23).setValue(""+Calendar.getInstance().get(Calendar.DAY_OF_YEAR));

        /*24 Field Name : Agreement_Month_Year*/
        fields.add(new Field());
        fields.get(24).setId("1608534508307");
        fields.get(24).setName("Agreement_Month_Year");
        fields.get(24).setRequired(false);
        fields.get(24).setType("text");
        fields.get(24).setValue(""+Calendar.getInstance().get(Calendar.YEAR));

        /*25 Field Name : Agreement_person_name*/
        fields.add(new Field());
        fields.get(25).setId("1608534700679");
        fields.get(25).setName("Agreement_person_name");
        fields.get(25).setRequired(false);
        fields.get(25).setType("text");
        fields.get(25).setValue(vendorRecord.getGrowerName()); //TODO Confirm name

        /*26 Field Name : details_of_location*/
        fields.add(new Field());
        fields.get(26).setId("1608534743553");
        fields.get(26).setName("details_of_location");
        fields.get(26).setRequired(false);
        fields.get(26).setType("text");
        fields.get(26).setValue(vendorRecord.getVillageName()); //TODO Confirm location

        /*27 Field Name : agreement_date*/
        fields.add(new Field());
        fields.get(27).setId("1608534834140");
        fields.get(27).setName("agreement_date");
        fields.get(27).setRequired(false);
        fields.get(27).setType("text");
        String dateStr = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + "-" + Calendar.getInstance().get(Calendar.MONTH) + "-" + Calendar.getInstance().get(Calendar.YEAR);
        fields.get(27).setValue(dateStr);

        /*28 Field Name : crop_name*/
        fields.add(new Field());
        fields.get(28).setId("1608540758485");
        fields.get(28).setName("crop_name");
        fields.get(28).setRequired(false);
        fields.get(28).setType("text");
        fields.get(28).setValue(vendorRecord.getCropDesc()); //TODO Confirm crop name

        /*29 Field Name : grow_area_acre*/
        fields.add(new Field());
        fields.get(29).setId("1608534947828");
        fields.get(29).setName("grow_area_acre");
        fields.get(29).setRequired(false);
        fields.get(29).setType("text");
        fields.get(29).setValue(vendorRecord.getBprdAcre()); //TODO Confirm grow_area_acre

        /*30 Field Name : year*/
        fields.add(new Field());
        fields.get(30).setId("1608535004043");
        fields.get(30).setName("year");
        fields.get(30).setRequired(false);
        fields.get(30).setType("text");
        fields.get(30).setValue(""+Calendar.getInstance().get(Calendar.YEAR));

        /*31 Field Name : season*/
        fields.add(new Field());
        fields.get(31).setId("1608537341918");
        fields.get(31).setName("season");
        fields.get(31).setRequired(false);
        fields.get(31).setType("text");
        fields.get(31).setValue(vendorRecord.getSeason());

        /*32 Field Name : level_of_moisture*/
        fields.add(new Field());
        fields.get(32).setId("1608535103774");
        fields.get(32).setName("level_of_moisture");
        fields.get(32).setRequired(false);
        fields.get(32).setType("text");
        fields.get(32).setValue(vendorRecord.getMoisture());

        /*33 Field Name : minimum_germination_percentage*/
        fields.add(new Field());
        fields.get(33).setId("1608535198491");
        fields.get(33).setName("minimum_germination_percentage");
        fields.get(33).setRequired(false);
        fields.get(33).setType("text");
        fields.get(33).setValue(vendorRecord.getGerminationStd());//TODO confirm

        /*34 Field Name : minimum_genetic_purity*/
        fields.add(new Field());
        fields.get(34).setId("1608535306751");
        fields.get(34).setName("minimum_genetic_purity");
        fields.get(34).setRequired(false);
        fields.get(34).setType("text");
        fields.get(34).setValue(vendorRecord.getGeneticPurityStd());//TODO minimum_genetic_purity

        /*35 Field Name : distance_of_area*/
        fields.add(new Field());
        fields.get(35).setId("1608535350114");
        fields.get(35).setName("distance_of_area");
        fields.get(35).setRequired(false);
        fields.get(35).setType("text");
        fields.get(35).setValue(vendorRecord.getIsolationDistanceM());//TODO distance_of_area

        /*36 Field Name : clause_amount*/
        fields.add(new Field());
        fields.get(36).setId("1608535506134");
        fields.get(36).setName("clause_amount");
        fields.get(36).setRequired(false);
        fields.get(36).setType("text");
        fields.get(36).setValue("");//TODO clause_amount

        /*37 Field Name : clause_amount_in_words*/
        fields.add(new Field());
        fields.get(37).setId("1608535534192");
        fields.get(37).setName("clause_amount_in_words");
        fields.get(37).setRequired(false);
        fields.get(37).setType("text");
        fields.get(37).setValue("");//TODO clause_amount_in_words

        /*38 Field Name : Name_of_first_party_representative*/
        fields.add(new Field());
        fields.get(38).setId("1608535972070");
        fields.get(38).setName("Name_of_first_party_representative");
        fields.get(38).setRequired(false);
        fields.get(38).setType("text");
        fields.get(38).setValue(inputsDC.firstPartyFullName);

        /*39 Field Name : grower_name*/
        fields.add(new Field());
        fields.get(39).setId("1608536082694");
        fields.get(39).setName("grower_name");
        fields.get(39).setRequired(false);
        fields.get(39).setType("text");
        fields.get(39).setValue(vendorRecord.getGrowerName());

        /*40 Field Name : grower_address*/
        fields.add(new Field());
        fields.get(40).setId("1608536106848");
        fields.get(40).setName("grower_address");
        fields.get(40).setRequired(false);
        fields.get(40).setType("text");
        fields.get(40).setValue(""); //TODO GET grower_address

        /*41 Field Name : grower_village*/
        fields.add(new Field());
        fields.get(41).setId("1608536128897");
        fields.get(41).setName("grower_village");
        fields.get(41).setRequired(false);
        fields.get(41).setType("text");
        fields.get(41).setValue(vendorRecord.getVillageName()); //TODO confirm grower_village

        /*42 Field Name : Grower_taluka_district*/
        fields.add(new Field());
        fields.get(42).setId("1608536167294");
        fields.get(42).setName("Grower_taluka_district");
        fields.get(42).setRequired(false);
        fields.get(42).setType("text");
        fields.get(42).setValue(vendorRecord.getTalukaName()+","+vendorRecord.getDistrictName()); //TODO confirm Grower_taluka_district

        /*43 Field Name : witness_one_name*/
        fields.add(new Field());
        fields.get(43).setId("1608536222501");
        fields.get(43).setName("witness_one_name");
        fields.get(43).setRequired(false);
        fields.get(43).setType("text");
        fields.get(43).setValue(inputsDC.getWitnessOneName());

        /*44 Field Name : witness_one_address*/
        fields.add(new Field());
        fields.get(44).setId("1608536275553");
        fields.get(44).setName("witness_one_address");
        fields.get(44).setRequired(false);
        fields.get(44).setType("text");
        fields.get(44).setValue(inputsDC.getWitnessOneAddress());

        /*45 Field Name : witness_one_village*/
        fields.add(new Field());
        fields.get(45).setId("1608536370418");
        fields.get(45).setName("witness_one_village");
        fields.get(45).setRequired(false);
        fields.get(45).setType("text");
        fields.get(45).setValue(inputsDC.getWitnessOneVillage());

        /*46 Field Name : witness_one_taluka_district*/
        fields.add(new Field());
        fields.get(46).setId("1608536463943");
        fields.get(46).setName("witness_one_taluka_district");
        fields.get(46).setRequired(false);
        fields.get(46).setType("text");
        fields.get(46).setValue(inputsDC.getWitnessOneDistrict()); //TODO confirm input Taluka too

        /*47 Field Name : witness_two_name*/
        fields.add(new Field());
        fields.get(47).setId("1608537341934");
        fields.get(47).setName("witness_two_name");
        fields.get(47).setRequired(false);
        fields.get(47).setType("text");
        fields.get(47).setValue(inputsDC.getWitnessTwoName());

        /*48 Field Name : witness_two_address*/
        fields.add(new Field());
        fields.get(48).setId("1608537341935");
        fields.get(48).setName("witness_two_address");
        fields.get(48).setRequired(false);
        fields.get(48).setType("text");
        fields.get(48).setValue(inputsDC.getWitnessTwoAddress());

        /*49 Field Name : witness_two_village*/
        fields.add(new Field());
        fields.get(49).setId("1608537341936");
        fields.get(49).setName("witness_two_village");
        fields.get(49).setRequired(false);
        fields.get(49).setType("text");
        fields.get(49).setValue(inputsDC.getWitnessTwoVillage());

        /*50 Field Name : witness_two_taluka_district*/
        fields.add(new Field());
        fields.get(50).setId("1608537341937");
        fields.get(50).setName("witness_two_taluka_district");
        fields.get(50).setRequired(false);
        fields.get(50).setType("text");
        fields.get(50).setValue(inputsDC.getWitnessTwoDistrict()); //TODO confirm input Taluka too

        /*51 Field Name : grower_name*/
        fields.add(new Field());
        fields.get(51).setId("1608535767854");
        fields.get(51).setName("grower_name");
        fields.get(51).setRequired(false);
        fields.get(51).setType("text");
        fields.get(51).setValue(vendorRecord.getGrowerName());

        /*52 Field Name : Name_of_first_party_representative*/
        fields.add(new Field());
        fields.get(52).setId("1608537341939");
        fields.get(52).setName("Name_of_first_party_repre");
        fields.get(52).setRequired(false);
        fields.get(52).setType("text");
        Log.d("NAME","Name_of_first_party_representative::"+inputsDC.firstPartyFullName);
        fields.get(52).setValue(inputsDC.firstPartyFullName);

        /*53 Field Name : grower_name*/
        fields.add(new Field());
        fields.get(53).setId("1608541254627");
        fields.get(53).setName("grower_name");
        fields.get(53).setRequired(false);
        fields.get(53).setType("text");
        fields.get(53).setValue(vendorRecord.getGrowerName()); //TODO confirm full name

        /*<<<<<<<<<<<<<<<<<<<<<<<<<====================Field Entry ENDS*====================>>>>>>>>>>>>>>>>>>>*/


        File file = new File();
        //file.setFile("");
        file.setName("");
        file.setFields(fields);

        config.setVerifyGender("");
        config.setVerifyName(false);
        config.setVerifyPincode("");
        config.setVerifySmartName(true);
        config.setVerifyState("");
        config.setVerifyTitle("");
        config.setVerifyYob(0);

        config1.setVerifyName(false);
        config1.setVerifyPincode("");
        config1.setVerifySmartName(true);
        config1.setVerifyState("");

        /*Invitee ONE GROWER*/
        invitees.add(new Invitee());
        invitees.get(0).setEmail("");
        invitees.get(0).setPhone(inputsDC.growerPhoneNo);
        invitees.get(0).setName(inputsDC.growerFullName);

        /*Invitee TWO First Party Representative*/
        invitees.add(new Invitee());
        invitees.get(1).setEmail("");
        invitees.get(1).setPhone(inputsDC.firstPartyPhoneNo);
        invitees.get(1).setName(inputsDC.firstPartyFullName);

        /*Invitee THREE Witness One*/
        invitees.add(new Invitee());
        invitees.get(2).setEmail("");
        invitees.get(2).setPhone(inputsDC.witnessOnePhoneNo);
        invitees.get(2).setName(inputsDC.witnessOneName);

        /*Invitee THREE Witness TWO*/
        invitees.add(new Invitee());
        invitees.get(3).setEmail("");
        invitees.get(3).setPhone(inputsDC.witnessTwoPhoneNo);
        invitees.get(3).setName(inputsDC.witnessTwoName);

        requestSign.setProfileId("FSVA8lF");
        requestSign.setFile(file);
        requestSign.setStampSeries("");
        requestSign.setInvitees(invitees);
        requestSign.setIrn("");
        Gson gson = new Gson();
        String json = gson.toJson(requestSign);
        Log.d("Leegality","JSON OBJECT : : "+ json);

        String result =  Utility.POSTJSON("", json,"");

        Log.d("Leegality","<<<======================================== Leegality START RESULT============================================>>>");
        Log.d("Leegality","RESULT::"+result);
        Log.d("Leegality","<<<======================================== Leegality END RESULT============================================>>>");

        iLeegalityHelper.getLeegalityResult(result);

        return result;
    }

}
