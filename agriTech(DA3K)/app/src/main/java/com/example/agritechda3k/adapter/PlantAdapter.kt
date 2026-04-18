package com.example.agritechda3k.adapter

import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agritechda3k.databinding.LayoutItemBinding
import com.example.agritechda3k.model.Plant
import com.example.agritechda3k.R

class PlantAdapter(
    private var plants: List<Plant>,
    private val onItemClick: (Plant) -> Unit
): RecyclerView.Adapter<com.example.agritechda3k.adapter.PlantAdapter.PlantViewHolder>() {
    // Khai báo vị trí được chọn để Fragment có thể lấy được dữ liệu của item đó
    var positionSelected: Int = -1

    inner class PlantViewHolder(val binding: LayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener {
        init {
            // Đăng ký Context Menu cho toàn bộ item
            binding.root.setOnCreateContextMenuListener(this)
        }

        fun bind(plant: Plant) {
            binding.apply {
                txtTen.text = "Tên: ${plant.namePlant ?: "Chưa có tên"}"
                txtMota.text = "Mô Tả: ${plant.descriptionPlant ?: "Không có mô tả"}"
                Glide.with(itemView.context)
                    .load(plant.thumbnailUrl)
                    .into(imgCay)

                btnChiTiet.setOnClickListener { onItemClick(plant) } // Lắng nghe click

                // Lưu lại vị trí khi nhấn giữ
                binding.root.setOnLongClickListener {
                    positionSelected = bindingAdapterPosition
                    Log.d("ADAPTER_CHECK", "Đã giữ item tại vị trí: $positionSelected")
                    false
                }
            }
        }


        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            // Tương tự ở đây nếu cần dùng vị trí
            val currentPlant = plants[bindingAdapterPosition]
            menu?.setHeaderTitle(currentPlant.namePlant)
            // Tạo các item menu
            // 2. Dùng MenuInflater để nạp file XML
            val inflater = MenuInflater(v?.context)
            inflater.inflate(R.menu.plant_context_menu,menu)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlantViewHolder {
        val v = LayoutItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PlantViewHolder((v))
    }
    override fun onBindViewHolder(
        holder: PlantViewHolder,
        position: Int
    ) {
        holder.bind(plants[position])
    }

    override fun getItemCount(): Int {
        return plants.size
    }
    fun setData(newPlant: List<Plant>) {
        this.plants = newPlant
        notifyDataSetChanged()

    }

    // Hàm tiện ích để lấy cây tại vị trí đang được nhấn giữ
    fun getSelectedPlant(): Plant? {
        return if (positionSelected != -1) plants[positionSelected] else null
    }


}