package com.winner.actorbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.winner.actorbook.databinding.RecyclerforactorBinding;

import java.util.ArrayList;

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.ActorHolder> {

    ArrayList<ActorList> actorLists;

    public ActorAdapter(ArrayList<ActorList> actorLists) {
        this.actorLists = actorLists;
    }

    @NonNull
    @Override
    public ActorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerforactorBinding recyclerforactorBinding=RecyclerforactorBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ActorHolder(recyclerforactorBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorHolder holder, int position) {
        holder.binding.recycleradaptor.setText(actorLists.get(position).name);

    }

    @Override
    public int getItemCount() {
        return actorLists.size();
    }

    public class ActorHolder extends RecyclerView.ViewHolder{
        private RecyclerforactorBinding binding;
        public ActorHolder(RecyclerforactorBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
