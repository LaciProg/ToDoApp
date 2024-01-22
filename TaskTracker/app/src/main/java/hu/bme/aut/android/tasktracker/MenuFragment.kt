package hu.bme.aut.android.tasktracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.tasktracker.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
    private lateinit var binding : FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnList.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_listFragment)
        }

        binding.btnNew.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_newFragment)
        }
    }
}