package edu.nuce.cinema.ui.common.events

import edu.nuce.cinema.data.models.Comment

interface CommentEvent {
    fun onLike(comment: Comment)
    fun onParentComment(comment: Comment)
    fun onReport(comment: Comment)
}