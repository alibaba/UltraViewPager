[中文API](ATTRIBUTES-ch.md)
### initIndicator
Constructs a indicator with no options. This indicator supports calling set-method in chained mode. The arrtibutes of `focusColor` and `normalColor` are necessary, or the indicator won't be shown.

#### API
``` 
IUltraIndicatorBuilder initIndicator();
```
---
### setFocusColor setNormalColor
Fill indicator with color.

`focusColor` Set focused color for indicator.
`normalColor` Set normal color for indicator.

#### API
``` 
IUltraIndicatorBuilder setFocusColor(int focusColor);
IUltraIndicatorBuilder setNormalColor(int normalColor);
```

```
ultraViewPager.getIndicator()
    .setFocusColor(Color.GREEN)
    .setNormalColor(Color.WHITE)
    .setRadius((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
```

![color](pics/20151203-0.png)

---
### setFocusResId setNormalResId
Fill indicator with bitmap or icon.
`focusResId` Set focused resource ID for indicator.
`normalResId` Set normal resource ID for indicator.

#### API
``` 
IUltraIndicatorBuilder setFocusResId(int focusResId);
IUltraIndicatorBuilder setNormalResId(int normalResId);
```
```
ultraViewPager.getIndicator()
.setFocusResId(R.mipmap.tm_biz_lifemaster_indicator_selected)
.setNormalResId(R.mipmap.tm_biz_lifemaster_indicator_normal);
```
![icon](pics/20151203-1.png)
---
### setIndicatorPadding

`indicatorPadding` sets spacing between indicator items，the default value is item's height.


#### API
``` 
IUltraIndicatorBuilder setIndicatorPadding(int indicatorPadding);
```


---
### build
After setting the indicator feature, call `build()` to complete. 

#### API
``` 
void build();
```
---
### setMultiScreen
set multi-screen feature, the width of the child view won't occupy full screen.
#### API
```
void setMultiScreen(float ratio)
```

```
ultraViewPager.setMultiScreen(0.5f);
ultraViewPager.setItemRatio(1.0f);
ultraViewPager.setAutoMeasureHeight(true);
```
<img src="pics/api2.png" width="200px" />

---
### setAutoMeasureHeight
When enabled, the height of the UltraViewPager will be automatically adjusted to the height of child view.
Do not use `setRadio` and `setAutoMeasureHeight` at the same time.
#### API
```
void setAutoMeasureHeight(boolean enable)
```  

```
ultraViewPager.setMultiScreen(1.0f);//single screen
ultraViewPager.setItemRatio(1.0f);//the aspect ratio of child view equals to 1.0f
ultraViewPager.setAutoMeasureHeight(false);
```
<img src="pics/api0.png" width="200px" />

```
ultraViewPager.setMultiScreen(1.0f);
ultraViewPager.setItemRatio(1.0f);
ultraViewPager.setAutoMeasureHeight(true);
```
<img src="pics/api1.png" width="200px" />

---
### setItemRatio
Adjust the height of child view with aspect `ratio`.
#### API

```
void setItemRatio(double ratio)
``` 

---
### setRatio
Draw UltraViewPager with the aspect `ratio`. The priority of `setRatio` is higher than `setItemRatio`
#### API
```
void setRatio(float ratio)
```
```
ultraViewPager.setMultiScreen(1.0f);//single screen
ultraViewPager.setRatio(2.0f);//the aspect ratio of viewpager  equals to 2.0f
ultraViewPager.setAutoMeasureHeight(true);
```

<img src="pics/api3.png" width="200px" />

