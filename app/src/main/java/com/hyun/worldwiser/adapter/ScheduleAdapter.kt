package com.hyun.worldwiser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.hyun.worldwiser.R
import com.hyun.worldwiser.model.Schedule

class ScheduleAdapter(val context: Context, private val scheduleList: ArrayList<Schedule>, val layoutInflater: LayoutInflater) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_travel_schedule_list, parent, false)
        val bottomSheetSchedulePlanCheckView = LayoutInflater.from(parent.context).inflate(R.layout.dialog_bottom_sheet_travel_plan_check, parent, false)

        return ViewHolder(view, bottomSheetSchedulePlanCheckView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(scheduleList[position])
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    inner class ViewHolder(itemView: View, private val bottomSheetSchedulePlanCheckView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(schedule: Schedule) {

            val bottomSheetSchedulePlanCheckDialog = BottomSheetDialog(context)
            bottomSheetSchedulePlanCheckDialog.setContentView(bottomSheetSchedulePlanCheckView)

            val todo = schedule.todo
            val todoDate = schedule.todoDate
            val status = schedule.status
            val category = schedule.category

            itemView.findViewById<TextView>(R.id.tv_travel_schedule_todo).text = todo
            itemView.findViewById<TextView>(R.id.tv_travel_schedule_todoDate).text = todoDate
            itemView.findViewById<TextView>(R.id.tv_travel_schedule_category).text = category
            itemView.findViewById<TextView>(R.id.tv_travel_schedule_status).text = status

            itemView.setOnClickListener {
                if (itemView.findViewById<TextView>(R.id.tv_travel_schedule_status).text == "진행 완료") {
                    Toast.makeText(context, "이미 진행 완료 상태기 때문에, 수정 불가능 합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    bottomSheetSchedulePlanCheckDialog.show()
                }
            }
        }
    }
}