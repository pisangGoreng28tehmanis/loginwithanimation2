import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.response.ListStoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoryViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _stories = MutableStateFlow<List<ListStoryItem>>(emptyList())
    private var token: String? = null

    fun setToken(token: String?) {
        this.token = token
        // Log token ketika diset
        Log.d("StoryViewModel", "Token set: $token")
    }

    val stories: StateFlow<List<ListStoryItem>> = _stories

    fun fetchStories() {
        viewModelScope.launch {
            if (token.isNullOrEmpty()) {
                _stories.value = emptyList()
                return@launch
            }

            try {
                // Fetch the stories from the repository
                val response = repository.getStories(token ?: "")
                _stories.value = response.listStory?.filterNotNull() ?: emptyList() // Filter out null values
            } catch (e: Exception) {
                _stories.value = emptyList() // Handle error gracefully
                Log.e("StoryViewModel", "Error fetching stories: ${e.message}")
            }
        }
    }
}
