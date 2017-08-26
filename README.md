# MarkChat

[apk](https://github.com/Zzzia/Files/blob/master/apks/MarkChat.apk)

这本来应该是个基于markdown的聊天软件，奈何最后被我搞蹦了，其实markdown不论便捷还是显示效果都还可以，只是这次真的翻车了。。

现在版本换成了没有markdown，没有后台接收消息的版本。。

但是还是来介绍一下我这几天的心血吧。。

真的非常用心了，花了非常多的时间在细节处理上，想把用户体验提高。

---

因为ui的关系，操作感觉是有点不清晰，重来没有做成过这样子的ui。

如何添加好友：聊天界面右上角查找用户，点击item进入对话，点item左侧的头像就可以添加好友了，当然也可以在聊天对话中点击好友的头像添加好友。

如何删除好友或聊天记录：当然是item左滑删除，没有任何提示，感觉不会有人知道可以有这个操作。

---

先看一下和聊天相关的，可以录音，可以传照片，拍摄照片或视频

有未读消息，进入对话中清除

每条消息有时间，根据不同时间显示不同，如今天，一分钟前，星期几。

如果聊天记录很多的话，顶部会有“点击加载更多”的按钮出现，如果聊天记录不多则会自动隐藏。

如果两个对话消息超过3分钟，则在对话中显示时间，否则不显示


<img src="https://github.com/Zzzia/Files/blob/master/imgs/MarkChat/demo.gif"/>

<img src="https://github.com/Zzzia/Files/blob/master/imgs/MarkChat/%E6%97%B6%E9%97%B4%E5%AF%B9%E6%AF%94.png" width="360" height="640" />

<img src="https://github.com/Zzzia/Files/blob/master/imgs/MarkChat/%E6%97%B6%E9%97%B4%E5%AF%B9%E6%AF%941.png" width="360" height="640" />


---

可以群聊，群聊时显示其他人的昵称，隐藏自己的昵称，大大节省屏幕空间，群聊可以使用以上所有功能



---

所有activity跳转采用共享元素动画过度，本来是想做很多动画的，结果时间没有这么多

从登录注册开始，所有activity我看做一张纸片在不断变换

这个想法来源于两张官方的gif

<img src="https://github.com/Zzzia/Files/blob/master/imgs/MarkChat/%E6%95%88%E6%9E%9C0.gif" width="360" height="640" />

<img src="https://github.com/Zzzia/Files/blob/master/imgs/MarkChat/%E6%95%88%E6%9E%9C.gif"/>

我觉得谷歌的意思应该是想把ui基于一张圆润方正的CardView来做界面，于是我在这次考核中全程使用CardView，变换又代表着transition动画，所以我在处理页面跳转逻辑时全部是从一个cardView共享元素跳转到另一个activity的cardview，这样一来，所有的用户操作看起来就像是在将纸片进行缩放，同时动画改变纸片内容。

于是这次考核的ui写成了这样子

<img src="https://github.com/Zzzia/Files/blob/master/imgs/MarkChat/demo1.gif" width="360" height="640" />

我也不知道这样好不好看，我觉得还行吧，效果虽然达不到我的预期，实力有限，但是还是有点想要的感觉

因为目前几乎所有app都还是以前那样的fade,explode，这些常见且没有特色的跳转，而且我觉得完全没有符合谷歌所说的魔法纸片的效果，于是这次考核我决定自己来写这么一个基于魔法纸片的app。于是最开始我把包名定做MagicCard，后来为了特色和符合考核主题，换成了MarkChat。

当然，也因为这样的ui，让交互逻辑变得有点迷。。以后可以好好改进一下
