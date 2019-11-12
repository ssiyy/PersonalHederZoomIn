# PersonalHederZoomIn
个人主页放大缩小的项目

## Demo截图
![demo](./img/demo.gif)

个人主页的图片展示可以随着用户的下拉而被放大。

那么这个是怎么实现的呢？其实实现方式很简单AppBarLayout+CollapsingToolbarLayout+Toolbar+Behavior。

那难点是那么呢？难点是个支持库版本里面AppBarLayout的实现效果是不一样的。

我项目用的支持包是“27.1.1”版本，放大和缩小的效果有卡顿，有时候放大了就不缩小了。经过网上查找发现支持包“25.3.1”实现效果是我想要的。我不可能项目的支持包为了这一个效果而改成“25.3.1”，我只能把涉及到APPBarLayout的关键代码拷贝一份到项目中去，在多番尝试之后我把所有涉及的关键类都找出来了

