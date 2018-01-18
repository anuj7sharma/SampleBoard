package com.sampleboard.view.fragment.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sampleboard.R;
import com.sampleboard.adapter.CommentAdapter;
import com.sampleboard.bean.api_response.GetCommentsResponse;
import com.sampleboard.databinding.FragmentCommentBinding;
import com.sampleboard.utils.Constants;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.BaseFragment;
import com.sampleboard.view.activity.DetailActivityV2;
import com.sampleboard.viewmodel.CommentViewModel;

import java.util.LinkedList;
import java.util.List;

/**
 * @author AnujSharma on 12/27/2017.
 */

public class CommentFragment extends BaseFragment implements CommentAdapter.CommentInterface, View.OnClickListener {
    private CommentViewModel viewModel;
    private FragmentCommentBinding binding;
    private LinkedList<GetCommentsResponse.DataBean> messageList;
    private CommentAdapter adapter;
    private String postId = "";
    private int page = 1;
    private GetCommentsResponse.DataBean singleMessageObj;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment, container, false);
        viewModel = ViewModelProviders.of(this).get(CommentViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        //get Post Id first
        if (getArguments() != null) {
            postId = String.valueOf(getArguments().getInt(Constants.EXTRA_POST_ID));
        }
        if (Utils.isNetworkAvailable(getActivity())) viewModel.getComments(postId, page);
        else
            Utils.getInstance().showSnakBar(binding.getRoot(), getString(R.string.error_internet));

        subscribeObservers();
    }

    private void subscribeObservers() {
        if (viewModel != null) {
            viewModel.getMessage().observe(this, message -> {
                if (!TextUtils.isEmpty(message))
                    Utils.getInstance().showSnakBar(binding.getRoot(), message);
            });

            //Observer for message response
            viewModel.getGetCommentResponse().observe(this, messageResponse -> {
                if (messageResponse != null) {
                    if (messageResponse.getCode() == 1) {
                        manageMessageList(messageResponse.getData());
                    } else {
                        Utils.getInstance().showSnakBar(binding.getRoot(), messageResponse.getMessage());
                    }
                }
            });

            //Observer for post comment
            viewModel.getPostCommentResponse().observe(this, postComment -> {
                if (postComment != null) {
                    if (postComment.getCode() == 1) {
                        //Add Comment to Existing list
                        if (messageList == null) messageList = new LinkedList<>();
                        if (singleMessageObj != null)
                            messageList.add(singleMessageObj);
                        adapter.updateList(messageList);
                        adapter.notifyItemInserted(messageList.size() - 1);

                        binding.emptyLayout.setVisibility(View.GONE);
                    } else {
                        Utils.getInstance().showSnakBar(binding.getRoot(), postComment.getMessage());
                    }
                }
            });
        }
    }

    private void manageMessageList(List<GetCommentsResponse.DataBean> data) {
        if (messageList == null)
            messageList = new LinkedList<>();

        if (data.size() > 0) {
            messageList.addAll(data);
            binding.emptyLayout.setVisibility(View.GONE);
            adapter.updateList(messageList);
        } else if (messageList.size() == 0 && data.size() > 0) {
            //show empty view
            binding.emptyLayout.setVisibility(View.VISIBLE);
        }

    }

    private void initViews() {
        if (getActivity() instanceof DetailActivityV2) {
            ((DetailActivityV2) getActivity()).setSupportActionBar(binding.includeToolbar.toolbar);
            ((DetailActivityV2) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((DetailActivityV2) getActivity()).getSupportActionBar().setTitle("Comments");
            binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_close);
            binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> ((DetailActivityV2) getActivity()).oneStepBack());
        }
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerComment.setLayoutManager(lm);
        adapter = new CommentAdapter(getActivity(), null, this);
        binding.recyclerComment.setAdapter(adapter);

        /*try {
            Gson gson = new Gson();
            CommentBean commentBean = gson.fromJson(getStringFromLocalJson("comment_list.json", getActivity()), CommentBean.class);
            if (commentBean.getData().getComment_list().size() > 0) {
                adapter.updateList(commentBean.getData().getComment_list());
                binding.emptyLayout.setVisibility(View.GONE);
            } else {
                binding.emptyLayout.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //set click listener
        binding.btnSend.setOnClickListener(this);
    }

    @Override
    public void onProfileClick() {

    }

    @Override
    public void onLikeUnLikeClick() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                if (!TextUtils.isEmpty(Utils.getInstance().getUserId(getActivity()))) {
                    if (binding.etMessage.getText().toString().trim().length() > 0) {
                        if (Utils.isNetworkAvailable(getActivity())) {
                            if (viewModel != null) {
                                if (singleMessageObj == null)
                                    singleMessageObj = new GetCommentsResponse.DataBean();
                                singleMessageObj.setComment(binding.etMessage.getText().toString());
                                singleMessageObj.setCommentType(CommentAdapter.CommentType.TEXT.toString());
                                singleMessageObj.setUserName("Anuj");
                                singleMessageObj.setUserProfile("");
                                viewModel.postComment(binding.etMessage.getText().toString(), CommentAdapter.CommentType.TEXT, postId, "11");
                            }
                        } else {
                            Utils.getInstance().showSnakBar(binding.getRoot(), getString(R.string.error_internet));
                        }
                    }
                } else {
                    Utils.getInstance().showSnakBar(binding.getRoot(), "You have to do login first.");
                }

                break;
        }
    }


}
