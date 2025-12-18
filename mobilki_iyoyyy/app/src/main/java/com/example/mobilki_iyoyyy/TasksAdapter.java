package com.example.mobilki_iyoyyy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private List<Task> taskList;

    public TasksAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item5, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task t = taskList.get(position);

        holder.title.setText(t.title);
        holder.question.setText(t.qestion);

        holder.rb1.setText(t.option1);
        holder.rb2.setText(t.option2);
        holder.rb3.setText(t.option3);

        // очищаем состояние, если view переиспользована
        holder.itog.setText("");
        holder.rb1.setEnabled(true);
        holder.rb2.setEnabled(true);
        holder.rb3.setEnabled(true);
        holder.buttonText.setEnabled(true);
        holder.radioGroup.clearCheck();

        holder.buttonText.setOnClickListener(v -> {
            RadioButton checked = holder.itemView.findViewById(
                    holder.radioGroup.getCheckedRadioButtonId()
            );

            if (checked == null) {
                holder.itog.setText("Выберите вариант");
                return;
            }

            String userAnswer = checked.getText().toString();
            sendAnswerToServer(t.id_tasks, userAnswer, holder);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView title, question, buttonText, itog;
        RadioGroup radioGroup;
        RadioButton rb1, rb2, rb3;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskTitle);
            question = itemView.findViewById(R.id.taskQuestion);
            radioGroup = itemView.findViewById(R.id.taskOptions);
            rb1 = itemView.findViewById(R.id.rbOption1);
            rb2 = itemView.findViewById(R.id.rbOption2);
            rb3 = itemView.findViewById(R.id.rbOption3);
            buttonText = itemView.findViewById(R.id.buttonText);
            itog = itemView.findViewById(R.id.itogAnswerUsera);
        }
    }

    // СЕТЬ
    private void sendAnswerToServer(int taskId, String answer, TaskViewHolder holder) {

        ApiService api = RetrofitClient.getApiService();

        AnswerRequest req = new AnswerRequest(taskId, answer);

        api.sendAnswer(req).enqueue(new Callback<AnswerResponse>() {
            @Override
            public void onResponse(Call<AnswerResponse> call, Response<AnswerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body().result;

                    holder.itog.setText(result);

                    if (result.equals("Правильно")) {
                        holder.itog.setTextColor(android.graphics.Color.GREEN);
                    } else {
                        holder.itog.setTextColor(android.graphics.Color.RED);
                    }


                } else {
                    holder.itog.setText("Ошибка сервера");
                }
            }

            @Override
            public void onFailure(Call<AnswerResponse> call, Throwable t) {
                holder.itog.setText("Ошибка соединения");
            }
        });
    }
}
