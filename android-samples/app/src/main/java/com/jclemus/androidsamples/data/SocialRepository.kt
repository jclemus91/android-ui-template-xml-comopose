package com.jclemus.androidsamples.data

import com.jclemus.androidsamples.domain.User

class SocialRepository {

    private val users = mapOf(
        1 to User(id = 1, name = "Julio Lemus", username = "jclemus", friends = listOf<Long>(1, 2)),
        2 to User(id = 2, name = "Jared Pruitt", username = "jp", friends = listOf<Long>(2, 3)),
        3 to User(id = 3, name = "Leyla Jacobson", username = "lj", friends = listOf<Long>(3, 4, 5)),
        4 to User(id = 4, name = "Gregory Gomez", username = "gg", friends = listOf<Long>(1, 2)),
        5 to User(id = 5, name = "Zariyah Short", username = "zs", friends = listOf<Long>(5,6,7,8,9)),
        6 to User(id = 6, name = "Lawrence Goodman", username = "lg", friends = listOf<Long>(9)),
        7 to User(id = 7, name = "Royalty Zimmerman", username = "rz", friends = listOf<Long>(6)),
        8 to User(id = 8, name = "Yareli Craig", username = "rc", friends = listOf<Long>(1, 7)),
        9 to User(id = 9, name = "Ariya Palmer", username = "ap", friends = listOf<Long>(8,9))
    )
}