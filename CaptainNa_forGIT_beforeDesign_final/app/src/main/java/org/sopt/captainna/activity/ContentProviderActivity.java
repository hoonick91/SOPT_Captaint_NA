package org.sopt.captainna.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.sopt.captainna.R;
import org.sopt.captainna.dialog.AddPersonDialog;
import org.sopt.captainna.model.Contacts;

import java.util.ArrayList;

import static org.sopt.captainna.activity.CommonVariable.dataList;

public class ContentProviderActivity extends Activity {

    //어레이리스트
    private ArrayList<Contacts> contacts;
    private ArrayList<Contacts> temp;
    private EditText search_edit;
    private char firstchar;
    private static final char[] CHO =
            {0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139, 0x3141,
                    0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148,
                    0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e};

    //리아이클러 설정
    public RecyclerView recyclerView;
    public RecyclerAdapter RecyclerAdapter;
    private LinearLayoutManager layoutManager;
    private LinearLayout complete_btn;
    private LinearLayout back_btn;
    private LinearLayout choice_btn;
    private ImageView choice_img;
    private AddPersonDialog dialog;

    private int event_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider);

        Intent intent = getIntent();
        event_id = intent.getExtras().getInt("event_id");

        //리사이클러뷰 새팅
        initRecycler();

        //가져온 전화번호부 세팅
        saveContacts();

        //어뎁터에 세팅
        RecyclerAdapter = new RecyclerAdapter(contacts);
        recyclerView.setAdapter(RecyclerAdapter);
        search_edit = (EditText)findViewById(R.id.search_edit);

        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // 변경 전

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // 변경 되는 순간
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //입력이 끝난 후 처리를 담당
                String text = search_edit.getText().toString().toLowerCase();
                RecyclerAdapter.filter(text);
            }
        });

        back_btn = (LinearLayout) findViewById(R.id.close);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContentProviderActivity.this, EventJoinerActivity.class);
                startActivity(intent);
            }
        });

        complete_btn = (LinearLayout) findViewById(R.id.complete);
        complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Contacts> choiceContacts = RecyclerAdapter.getChoice();
                if (choiceContacts.size() != 0) {
                    dialog = new AddPersonDialog(ContentProviderActivity.this, choiceContacts, event_id);
                    dialog.show();
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                } else {
                    Toast.makeText(getApplicationContext(), "추가할 연락처를 선택해주세요!", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }


    /***********************************전화번호 저장*********************************************/
    private void saveContacts() {

        char name;
        char uniVal;
        contacts = new ArrayList<>();
        boolean noDiv[] = new boolean[] {true, true, true, true, true, true, true, true, true, true, true, true,
                true, true, true, true, true, true, true};
    /*
        for (int i = 0; i < dataList.size(); i++) {
            rvContacts.add(new Contacts(dataList.get(i).get("name"), dataList.get(i).get("phone"), getResources().getDrawable(R.drawable.participant_plus_check2), false));
        }
        */

        for (int i = 0; i < dataList.size(); i++) {
            name = dataList.get(i).get("name").charAt(0);
            uniVal = (char) (name - 0xAC00);
            firstchar = (char) (((uniVal - (uniVal % 28)) / 28) / 21); //자음따오기

            if (firstchar >= 0 && firstchar < 19) { //한글일때
                if (noDiv[firstchar]) { // 구분선('ㄱ' 같은거) 출력하지 않은경우
                    contacts.add(new Contacts(CHO[firstchar] + "", "", getResources().getDrawable(R.drawable.transparency), true, 0xFFFFFFFF, 0xFF638CA5));
                    noDiv[firstchar] = false;
                }
                contacts.add(new Contacts(dataList.get(i).get("name"), dataList.get(i).get("phone"), getResources().getDrawable(R.drawable.participant_plus_check1), false, 0xFFFFFFFF, 0xFF383838));
            }
        }

        contacts.add(new Contacts("기타", "", getResources().getDrawable(R.drawable.transparency), false, 0xFFFFFFFF, 0xFF638CA5));

        for (int i = 0; i < dataList.size(); i++) {

            name = dataList.get(i).get("name").charAt(0);
            uniVal = (char) (name - 0xAC00);
            firstchar = (char) (((uniVal - (uniVal % 28)) / 28) / 21); //자음따오기

            if (firstchar < 0 || firstchar >= 19) { //한글아닐때
                contacts.add(new Contacts(dataList.get(i).get("name"), dataList.get(i).get("phone"), getResources().getDrawable(R.drawable.participant_plus_check1), false, 0xFFFFFFFF, 0xFF383838));
            }
        }

    }


    /***********************************리사이클러 세팅*********************************************/
    private void initRecycler() {
        recyclerView = (RecyclerView) findViewById(R.id.subrecyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }


    /*****************************************전화번호부 어뎁터*******************************************/
    class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

        ArrayList<Contacts> rvContacts = new ArrayList<>();
        ArrayList<Contacts> filterdatas = new ArrayList<>();
        ArrayList<Contacts> choice = new ArrayList<>();

        int cnt = 0;


        public RecyclerAdapter(ArrayList<Contacts> contacts) {
            try {
                this.rvContacts = contacts;
                this.filterdatas.addAll(contacts);
                for(int i=0; i<filterdatas.size();i++){
                    if(filterdatas.get(i).phone == ""){//카테고리인경우
                        filterdatas.remove(i);
                        i -= 1;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }//rvContacts 연락처, temp??


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Contacts contact = rvContacts.get(position);

            holder.contact_name.setText(contact.name);
            holder.contact_phone.setText(contact.phone);
            holder.contact_check.setImageDrawable(contact.check);
            holder.contact_layout.setBackgroundColor(contact.background);
            holder.contact_name.setTextColor(contact.textColor);
            holder.contact_phone.setTextColor(contact.textColor);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!holder.contact_phone.getText().toString().equals("")) {//카테고리 클릭 걸러내기
                        if (rvContacts.get(position).background != 0xFFF1F1F1) {//선택안되어있을시(체크하기)
                            rvContacts.get(position).check = getResources().getDrawable(R.drawable.participant_plus_check2);
                            rvContacts.get(position).background = 0xFFF1F1F1;
                            rvContacts.get(position).textColor = 0xFF638CA5;
                            choice.add(rvContacts.get(position));

                        } else { //선택되어있는경우(체크풀기)
                            rvContacts.get(position).check = getResources().getDrawable(R.drawable.participant_plus_check1);
                            rvContacts.get(position).background = 0xFFFFFFFF;
                            rvContacts.get(position).textColor = 0xFF383838;
                            choice.remove(rvContacts.get(position));

                        }
                        notifyDataSetChanged();
                    }

                }
            });


            /*choice_img = (ImageView) findViewById(R.id.choiceimg);
            choice_btn = (LinearLayout) findViewById(R.id.choicebtn);
            choice_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choice_img.setImageResource(R.drawable.participant_all_check2);
                    Toast.makeText(getApplicationContext(), "구현예정", Toast.LENGTH_SHORT).show();
                }
            });*/
        }

        public void setContent(ArrayList<Contacts> itemdata) {
            this.rvContacts = itemdata;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return rvContacts != null ? rvContacts.size() - 1 + cnt : 0;
        }

        public  ArrayList<Contacts> getChoice(){
            return choice;
        }

        public void filter(String text) { //cnt??
            text = text.toLowerCase();
            cnt =0;
            temp = new ArrayList<>();
            if (text.length() == 0) {
                setContent(contacts);
            } else {
                cnt = 1;
                for (Contacts item : filterdatas) {
                    setContent(temp);
                    if (item.getItem_text().toLowerCase().contains(text)) {
                        temp.add(item);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }


    /*******************************************전화번호부 뷰홀더******************************************/
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView contact_name;
        TextView contact_phone;
        ImageView contact_check;
        LinearLayout contact_layout;

        public ViewHolder(View itemView) {
            super(itemView);

            contact_name = (TextView) itemView.findViewById(R.id.contact_name);
            contact_phone = (TextView) itemView.findViewById(R.id.contact_phone);
            contact_check = (ImageView) itemView.findViewById(R.id.contact_check);
            contact_layout = (LinearLayout) itemView.findViewById(R.id.contact_layout);
        }

    }
}