import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.housweet.presentation.ui.mypage.BookmarkItem

open class BookmarkViewModel : ViewModel() {
    // 샘플 데이터 - 나중에 Retrofit으로 대체
    private val _bookmarks = mutableStateListOf(
        BookmarkItem(1, "애완동물을 좋아하는 사람을 구하고 있어요.", "보증금 400 월세 20", "송파구 문정동", "20대 남자", true),
        BookmarkItem(2, "애완동물 좋아하는 사람을 구하고 있습니...", "보증금 400 월세 20", "송파구 문정동", "20대 남자", true)
    )
    open val bookmarks: List<BookmarkItem> get() = _bookmarks

    // 북마크 상태 토글
    fun toggleBookmark(item: BookmarkItem) {
        val index = _bookmarks.indexOfFirst { it.id == item.id }
        if (index != -1) {
            _bookmarks[index] = item.copy(bookmarked = !item.bookmarked)
        }

        // TODO: 나중에 이 부분에 서버 업데이트 요청 추가
        // updateBookmarkToServer(item.id, newStatus)
    }
}