import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.dti_admin.R
import com.example.dti_admin.databinding.FragmentAdminInfoBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AdminInfoFragment : Fragment() {

    private lateinit var binding: FragmentAdminInfoBinding
    private var imageURL: String? = null
    private var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                val data = result.data
                uri = data?.data
                binding.uploadSIImage.setImageURI(uri)
            } else {
                Toast.makeText(requireContext(), "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.uploadSIImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        binding.uploadSIBtn.setOnClickListener {
            saveData()
            binding.uploadSIImage.setImageResource(R.drawable.baseline_photo_size_select_actual_24)

        }

        binding.uploadSITextBtn.setOnClickListener {
            val text=binding.uploadSIText.text.toString()
            saveTextData(text)
            binding.uploadSIText.text.clear()

        }
    }

    private fun saveData() {
        if (uri != null) {
            val storageReference = FirebaseStorage.getInstance().reference
                .child("Share Info Images")
                .child(uri!!.lastPathSegment ?: "")

            val builder = AlertDialog.Builder(requireContext())
            builder.setCancelable(false)
            val dialogView = layoutInflater.inflate(R.layout.progress_layout, null)
            builder.setView(dialogView)
            val dialog = builder.create()
            dialog.show()

            val uploadTask = storageReference.putFile(uri!!)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storageReference.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    imageURL = downloadUri.toString()

                    pushToRealtimeDatabase(imageURL)
                    Toast.makeText(requireContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(requireContext(), "Upload Failed", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        } else {
            Toast.makeText(requireContext(), "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pushToRealtimeDatabase(imageURL: String?) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("AdminData")
                .child("ImageFolder").push().child("ImageURL")
            databaseReference.setValue(imageURL)
                .addOnSuccessListener {

                    Toast.makeText(
                        requireContext(),
                        "Image Uploaded Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {

                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                }
    }

    private fun saveTextData(text: String?) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("AdminData")
            .child("TextFolder").push().child("TextInfo")

        databaseReference.setValue(text)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Text Posted Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
