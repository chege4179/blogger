/*
 * Copyright 2023 Blogger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peterchege.blogger

import org.junit.Test

import org.junit.Assert.*
val all_posts_success = """
    {
      "msg": "All post fetched",
      "success": true,
      "posts": [
        {
          "_id": "626a3f2dc9b6caf4dabe9844",
          "postTitle": "About this app and its features ",
          "postBody": "So this app is like some sort of twitter and instagram but instead of posting just images you post stories (more like a blogging platform)\nIt has push notifications for new likes ,comments, followers \nThe posts can be deleted after posting them\nFor now the app is not published yet on the playstore ....I will publish it as soon as possible \nAs for the tech stack used \n1. Native Android for the client app\n2. Express.js/Node.is for the server application together with Mongo DB \n3. Cloudinary for storing the images \n4. Firebase messaging for push notifications\n",
          "postAuthor": "peterchege4179",
          "imageUrl": "https://res.cloudinary.com/dhuqr5iyw/image/upload/v1651130156/blogger/c70b8d5a835c512ec5ba1c2992caff89.jpg",
          "imageId": "c70b8d5a835c512ec5ba1c2992caff89",
          "postedAt": "10:15:44",
          "postedOn": "28/04/2022",
          "views": [
            {
              "viewerUsername": "peterchege4179",
              "viewerFullname": "kiswahili",
              "viewerId": "6201b23856618af34712b897"
            },
            {
              "viewerUsername": "testtest",
              "viewerFullname": "Test User",
              "viewerId": "62f3c0fb1ce10577669b6456"
            },
            {
              "viewerUsername": "sajalsj",
              "viewerFullname": "sajal jain",
              "viewerId": "6307569e947b5f37220b874b"
            },
            {
              "viewerUsername": "Chapo",
              "viewerFullname": "Chapo",
              "viewerId": "6360d7646d3491c16386e424"
            }
          ],
          "comments": [
            {
              "commentId": "1138e9a1053ce598bdd7e33ccbb54997",
              "comment": "Let me know if you find any bugs and also some feature requests",
              "username": "peterchege4179",
              "postedAt": "10:17:31",
              "postedOn": "28/04/2022",
              "userId": "6201b23856618af34712b897",
              "imageUrl": "https://ui-avatars.com/api/?background=A9C492&color=fff&name=Peter+Chege&bold=true&fontsize=0.6&rounded=true"
            },
            {
              "commentId": "1e7ba4af5788251013910d34d0f4d4bd",
              "comment": "The picture above is a piece of the source code of the app",
              "username": "peterchege4179",
              "postedAt": "10:18:46",
              "postedOn": "28/04/2022",
              "userId": "6201b23856618af34712b897",
              "imageUrl": "https://ui-avatars.com/api/?background=A9C492&color=fff&name=Peter+Chege&bold=true&fontsize=0.6&rounded=true"
            },
            {
              "commentId": "f66adb9687a018555f0fdd427ecfd785",
              "comment": "On God",
              "username": "peterchege4179",
              "postedAt": "04:18:19",
              "postedOn": "04/05/2022",
              "userId": "6201b23856618af34712b897",
              "imageUrl": "https://ui-avatars.com/api/?background=A9C492&color=fff&name=Peter+Chege&bold=true&fontsize=0.6&rounded=true"
            },
            {
              "commentId": "dd0c2a42df67950f45eada3c95465d06",
              "comment": "damn",
              "username": "peterchege4179",
              "postedAt": "07:41:33",
              "postedOn": "09/07/2022",
              "userId": "6201b23856618af34712b897",
              "imageUrl": "https://ui-avatars.com/api/?background=A9C492&color=fff&name=Peter+Chege&bold=true&fontsize=0.6&rounded=true"
            },
            {
              "commentId": "97e0c4d30d8958231ecbc170c1c12f31",
              "comment": "trapping",
              "username": "peterchege4179",
              "postedAt": "02:34:48",
              "postedOn": "27/07/2022",
              "userId": "6201b23856618af34712b897",
              "imageUrl": "https://ui-avatars.com/api/?background=A9C492&color=fff&name=Peter+Chege&bold=true&fontsize=0.6&rounded=true"
            },
            {
              "commentId": "ebf654160b3c4255e4be301af8c1ffc1",
              "comment": "vvvv",
              "username": "peterchege4179",
              "postedAt": "07:11:04",
              "postedOn": "10/08/2022",
              "userId": "6201b23856618af34712b897",
              "imageUrl": "https://ui-avatars.com/api/?background=A9C492&color=fff&name=Peter+Chege&bold=true&fontsize=0.6&rounded=true"
            },
            {
              "commentId": "7bdd910312c730bc2d731c94049703e1",
              "comment": "new comment",
              "username": "peterchege4179",
              "postedAt": "08:26:15",
              "postedOn": "19/08/2022",
              "userId": "6201b23856618af34712b897",
              "imageUrl": "https://ui-avatars.com/api/?background=A9C492&color=fff&name=Peter+Chege&bold=true&fontsize=0.6&rounded=true"
            },
            {
              "commentId": "7f35d61c32b0909e4469198db5c6aa52",
              "comment": "lol",
              "username": "sajalsj",
              "postedAt": "08:31:06",
              "postedOn": "25/08/2022",
              "userId": "6307569e947b5f37220b874b",
              "imageUrl": "https://ui-avatars.com/api/?background=98983B&color=fff&name=sajal+jain&bold=true&fontsize=0.6"
            },
            {
              "commentId": "cdb5c7ff7cd5e511f7cebd3f5a77f8b1",
              "comment": "hhhh",
              "username": "peterchege4179",
              "postedAt": "09:43:10",
              "postedOn": "28/08/2022",
              "userId": "6201b23856618af34712b897",
              "imageUrl": "https://ui-avatars.com/api/?background=A9C492&color=fff&name=Peter+Chege&bold=true&fontsize=0.6&rounded=true"
            },
            {
              "commentId": "2238504628ea154385e5d5ddd1930ad8",
              "comment": "Really ",
              "username": "Chapo",
              "postedAt": "11:23:41",
              "postedOn": "01/11/2022",
              "userId": "6360d7646d3491c16386e424",
              "imageUrl": "https://ui-avatars.com/api/?background=FA5FA0&color=fff&name=Chapo&bold=true&fontsize=0.6"
            },
            {
              "commentId": "6d33c2164ce77677b8b80b3221d5c45d",
              "comment": "hhhhhh",
              "username": "peterchege4179",
              "postedAt": "05:24:05",
              "postedOn": "07/01/2023",
              "userId": "6201b23856618af34712b897",
              "imageUrl": "https://ui-avatars.com/api/?background=A9C492&color=fff&name=Peter+Chege&bold=true&fontsize=0.6&rounded=true"
            }
          ],
          "likes": [
            {
              "username": "peterchege4179",
              "fullname": "kiswahili",
              "userId": "6201b23856618af34712b897"
            },
            {
              "username": "testtest",
              "fullname": "Test User",
              "userId": "62f3c0fb1ce10577669b6456"
            }
          ],
          "__v": 0
        },
        {
          "_id": "6360d8256d3491c16386e446",
          "postTitle": "Stop ",
          "postBody": "I have to divert to get a job and get out of work and work on the job and I am not going to be able to make it affordable and easy for MSMEs to do so I can be a good fit for the job and I am very excited about the opportunity to work with you and I look forward to engaging with you further and I look forward to engaging with you further and I look forward to engaging with you further and I look forward to engaging with you further and I look forward to engaging with you further and I look forward to engaging with you further and I look forward to engaging with you further and I look forward to engaging with you further and I look forward to ",
          "postAuthor": "Chapo",
          "imageUrl": "https://res.cloudinary.com/dhuqr5iyw/image/upload/v1667291172/blogger/c91431315e56022f8284ff98a656060d.jpg",
          "imageId": "c91431315e56022f8284ff98a656060d",
          "postedAt": "11:26:09",
          "postedOn": "01/11/2022",
          "views": [
            {
              "viewerUsername": "peterchege4179",
              "viewerFullname": "Peter Chege Mwangi",
              "viewerId": "6201b23856618af34712b897"
            },
            {
              "viewerUsername": "",
              "viewerFullname": "",
              "viewerId": ""
            }
          ],
          "comments": [],
          "likes": [
            {
              "username": "peterchege4179",
              "fullname": "Peter Chege Mwangi",
              "userId": "6201b23856618af34712b897"
            }
          ],
          "__v": 0
        },
        {
          "_id": "6472765c3b7bd0fc73ac6976",
          "postTitle": "gggg",
          "postBody": "ggggg",
          "postAuthor": "peterchege4179",
          "imageUrl": "https://res.cloudinary.com/dhuqr5iyw/image/upload/v1685223003/blogger/a8107b6880895f9bc6f3b779947e5734.jpg",
          "imageId": "a8107b6880895f9bc6f3b779947e5734",
          "postedAt": "12:30:00",
          "postedOn": "28/05/2023",
          "views": [
            {
              "viewerUsername": "peterchege4179",
              "viewerFullname": "Peter Chege Mwangi",
              "viewerId": "6201b23856618af34712b897"
            }
          ],
          "comments": [],
          "likes": [],
          "__v": 0
        },
        {
          "_id": "647727a07deaba85c05fe003",
          "postTitle": "deadpool ",
          "postBody": "nrbrhdhdhdbdbdhd",
          "postAuthor": "peterchege4179",
          "imageUrl": "https://res.cloudinary.com/dhuqr5iyw/image/upload/v1685530527/blogger/b0cf356448829d52011fb5075ebb6de7.jpg",
          "imageId": "b0cf356448829d52011fb5075ebb6de7",
          "postedAt": "01:55:19",
          "postedOn": "31/05/2023",
          "views": [
            {
              "viewerUsername": "peterchege4179",
              "viewerFullname": "Peter Chege Mwangi",
              "viewerId": "6201b23856618af34712b897"
            }
          ],
          "comments": [
            {
              "commentId": "8eab53581da8e04cc3feea940c765473",
              "comment": "bdbdndn",
              "username": "peterchege4179",
              "postedAt": "01:55:51",
              "postedOn": "31/05/2023",
              "userId": "6201b23856618af34712b897",
              "imageUrl": "https://ui-avatars.com/api/?background=A9C492&color=fff&name=Peter+Chege&bold=true&fontsize=0.6&rounded=true"
            }
          ],
          "likes": [
            {
              "username": "peterchege4179",
              "fullname": "Peter Chege Mwangi",
              "userId": "6201b23856618af34712b897"
            }
          ],
          "__v": 0
        },
        {
          "_id": "6495e39812ce86e490c1340a",
          "postTitle": "hhhgg",
          "postBody": "bbbbv",
          "postAuthor": "peterchege4179",
          "imageUrl": "https://res.cloudinary.com/dhuqr5iyw/image/upload/v1687544727/blogger/fe5942cac66b6ea1f0236edf916b399a.jpg",
          "imageId": "fe5942cac66b6ea1f0236edf916b399a",
          "postedAt": "09:25:23",
          "postedOn": "23/06/2023",
          "views": [],
          "comments": [],
          "likes": [],
          "__v": 0
        },
        {
          "_id": "6496bc857f7bacc32dbce146",
          "postTitle": "hhhh",
          "postBody": "vvghggggg",
          "postAuthor": "peterchege4179",
          "imageUrl": "https://res.cloudinary.com/dhuqr5iyw/image/upload/v1687600261/blogger/4fc1e21392a6f5430db208918cb0ad32.jpg",
          "imageId": "4fc1e21392a6f5430db208918cb0ad32",
          "postedAt": "12:50:56",
          "postedOn": "24/06/2023",
          "views": [],
          "comments": [],
          "likes": [],
          "__v": 0
        }
      ]
    }

   
""".trimIndent()


val all_posts_error = """
    {
      "msg": "An unexpected error occurred",
      "success": false,
      "posts": null

    }
""".trimIndent()