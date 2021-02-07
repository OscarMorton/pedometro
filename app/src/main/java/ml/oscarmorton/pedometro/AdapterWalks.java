package ml.oscarmorton.pedometro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterWalks extends RecyclerView.Adapter<AdapterWalks.WalksViewHolder> {
    private ArrayList<Walk> data;

    public AdapterWalks(ArrayList<Walk> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public WalksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_walk, parent, false);

        return new WalksViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WalksViewHolder holder, int position) {
        Walk walk = data.get(position);
        holder.bindWalks(walk);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class WalksViewHolder extends RecyclerView.ViewHolder {
        private TextView tvWalk, tvLocation, tvSteps;


        public WalksViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWalk = itemView.findViewById(R.id.tvWalk);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvSteps = itemView.findViewById(R.id.tvSteps);

        }

        public void bindWalks(Walk walk) {
            tvWalk.setText(walk.getName());
            tvLocation.setText(walk.getLocation());
            tvSteps.setText(String.valueOf(walk.getSteps()));

        }


    }


}
