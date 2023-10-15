package app.jsapps.whatsapp.view.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.jsapps.whatsapp.databinding.FragmentChatsBinding
import app.jsapps.whatsapp.view.NewChatActivity
import app.jsapps.whatsapp.view.adapters.recentMessages.MessageListAdapter
import app.jsapps.whatsapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatsFragment : Fragment() {

    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!

    private val adapter = MessageListAdapter()

    private val userVM:UserViewModel by viewModels()
    private val auth:FirebaseAuth = FirebaseAuth.getInstance()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {


            rvChats.adapter = adapter
            rvChats.layoutManager = LinearLayoutManager(requireContext())

            userVM.getRecentChats(auth.currentUser!!.email!!)
            Log.w(TAG, auth.currentUser?.email!!)

            lifecycleScope.launch {
                userVM.recentChats.collect{
                    if(it.isNotEmpty()){
                        Log.w(TAG, "true")
                        adapter.users = it
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            newMsg.setOnClickListener {
                Intent(requireActivity(), NewChatActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }


}