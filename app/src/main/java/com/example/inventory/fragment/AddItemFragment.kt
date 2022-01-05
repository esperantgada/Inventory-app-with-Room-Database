/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.inventory.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.inventory.application.InventoryApplication
import com.example.inventory.data.Item
import com.example.inventory.databinding.FragmentAddItemBinding
import com.example.inventory.model.ItemViewModel
import com.example.inventory.model.ItemViewModelFactory

/**
 * Fragment to add or update an item in the Inventory database.
 */
class AddItemFragment : Fragment() {

    //An instance of ItemViewModel
    private val viewModel : ItemViewModel by activityViewModels {
        ItemViewModelFactory((activity?.application as InventoryApplication
                ).database.itemDao())
    }

    //An object of type Item
    private lateinit var item : Item

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    // Binding object instance corresponding to the fragment_add_item.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Gets user inputs and checks if it is valid by calling [isEntryValid] method from
     * ItemViewModel class
     */
    private fun isUserInputValid() : Boolean{
        return viewModel.isEntryValid(
            binding.itemName.toString(),
            binding.itemPrice.toString(),
            binding.itemCount.toString()
        )
    }

    /**
     * If the user entries are valid, add them into the database by calling [addNewItem] method
     * from ItemViewModel class and navigate back to [ItemListFragment]
     */
    private fun addNewItemInDatabase(){
        if (isUserInputValid()){
            viewModel.addNewItem(
                binding.itemName.text.toString(),
                binding.itemPrice.text.toString(),
                binding.itemCount.text.toString()
            )

            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)
        }
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.saveAction.setOnClickListener { addNewItemInDatabase() }

        //retrieve itemId from the navigation arguments.
        val id = navigationArgs.itemId
        if (id > 0){
            viewModel.retrieveItem(id).observe(viewLifecycleOwner, { selectedItem ->
                item = selectedItem
                bind(item)
            })
        }else{
            binding.saveAction.setOnClickListener { addNewItemInDatabase() }
        }
    }

    /**
     * Binds an item textViews with its information when it's edited
     */
    private fun bind(item: Item){
        val price = "0.2%f".format(item.itemPrice)
        binding.apply {
            itemName.setText(item.itemName, TextView.BufferType.SPANNABLE)
            itemPrice.setText(price, TextView.BufferType.SPANNABLE)
            itemCount.setText(item.itemQuantityInStock.toString(), TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener { updateItem() }
        }
    }

    /**
     * Takes updated information from user and updates item in database
     */
    private fun updateItem(){
        if (isUserInputValid()){
            viewModel.updateItemInDatabase(
                this.navigationArgs.itemId,
                binding.itemName.text.toString(),
                binding.itemPrice.text.toString(),
                binding.itemCount.text.toString()
            )
            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
            this.findNavController().navigate(action)
        }
    }
}
