package tx.a316.com.tx_teacher.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.activites.MapActivity;
import tx.a316.com.tx_teacher.activites.PushActivity;
import tx.a316.com.tx_teacher.activites.RejoinHistoryListActivity;
import tx.a316.com.tx_teacher.activites.ReplyActivity;
import tx.a316.com.tx_teacher.activites.ReviewListActivity;


public class WorkFragment extends Fragment {
    Unbinder unbinder;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_work, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.work_pinglun, R.id.work_push, R.id.work_FaQiDabian, R.id.work_ChaKanDabian, R.id.work_Map})
    public void onViewClicked(View view) {
        Intent intent=new Intent();
        switch (view.getId()) {
            case R.id.work_pinglun:
                //跳转至评论周记列表界面
                 intent = new Intent(getActivity(), ReviewListActivity.class);
                break;
            case R.id.work_push:
                //跳转至发送通知界面
                intent = new Intent(getActivity(), PushActivity.class);
                break;
            case R.id.work_FaQiDabian:
                intent = new Intent(getActivity(),ReplyActivity.class);
                break;
            case R.id.work_ChaKanDabian:
                intent=new Intent(getActivity(), RejoinHistoryListActivity.class);
                break;
            case R.id.work_Map:
                intent = new Intent(getActivity(),MapActivity.class);
                break;
        }
        //跳转至相应页面
        getActivity().startActivity(intent);
    }
}
