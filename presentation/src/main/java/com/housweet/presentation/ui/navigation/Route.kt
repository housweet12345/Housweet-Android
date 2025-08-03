package com.housweet.presentation.ui.navigation
import com.housweet.domain.model.Coordinate
import kotlinx.serialization.Serializable

sealed interface Route {
    sealed interface StartPageRoute: Route {
        sealed interface LoginRoute : StartPageRoute {
            @Serializable
            data object Login : LoginRoute

            @Serializable
            data object WelCome : LoginRoute

            @Serializable
            data object PermissionGuide : LoginRoute

            @Serializable
            data object TermsOfService : LoginRoute
        }

        sealed interface AccessRoomRoute : StartPageRoute {
            @Serializable
            data object AccessRoom : AccessRoomRoute

            @Serializable
            data object CreateRoom : AccessRoomRoute

            @Serializable
            data object SearchRoom : AccessRoomRoute
        }
    }

    sealed interface CommunityPageRoute: Route {
        @Serializable
        data class Map(val coordinate: Coordinate? = null) : CommunityPageRoute

        @Serializable
        data object SearchRegion : CommunityPageRoute

        sealed interface PostRoute : CommunityPageRoute {
            @Serializable
            data object DetailPost : PostRoute

            @Serializable
            data object Posts: PostRoute
        }
    }

    sealed interface MainPageRoute: Route {
        @Serializable
        data object Home : MainPageRoute
        
        @Serializable 
        data object Calendar : MainPageRoute
        
        sealed interface ScheduleRoute : MainPageRoute {
            @Serializable
            data object TaskAdd : ScheduleRoute
            
            @Serializable
            data class TaskEdit(val taskTitle: String) : ScheduleRoute
            
            @Serializable
            data object MyTodoEdit : ScheduleRoute
        }
        
        @Serializable
        data object Notification : MainPageRoute
        
        @Serializable
        data class NoticeDetail(val noticeId: Int) : MainPageRoute
    }
}