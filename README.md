Extended ActionBar
===

The problem:
---
Android 4.4 Kitkat introduced a wonderful new opportunity: __translucent bars__.

It's as simple as adding the following to your theme:
```xml
<item name="android:windowTranslucentStatus">true</item>
<item name="android:windowTranslucentNavigation">true</item>
```
Awesome, right? Well, almost.<br>
The thing is, if you happen to have an ActionBar (like 99.9% of apps out there) than it won't automatically extend behind the status bar: the status bar will be translucent and the window background will be showed...<br>
A picture is worth a thousand words:

![Image](../master/imgs/problem.png?raw=true)

There are many ways to fix this programmatically or with some wise layouts, but when the application is launched or recreated only the theme will be used (before loading ANY code or layout) and the problem will still be annoyingly present.

The solution:
---
In this repository I show how to achieve an illusion of "ActionBar extension" (look at the code to see what it means).
It is unfortunately more of a hack than a proper solution, but that is due to the fact that the required components (namely <code>@android:dimen/status_bar_height</code> and the ability to create custom drawables, as the ones provided are barely usable and highly bugged) have been banned by the Gods.

_Please use it with careful consideration and don't blame me if your device explodes or you cat conquers the world!_

![Image](../master/imgs/solution.png?raw=true)

The extra mile:
---
While I was there I took the opportunity to create a simple <code>Activity</code> that removes the hacky window background and does things properly in order to eliminate any overdraw.<br>
It's supposed to be a starting point, though I hope to make it into a real library someday.

Before:

![Image](../master/imgs/overdraw_before.png?raw=true)

After:

![Image](../master/imgs/overdraw_after.png?raw=true)

Notes:
---
I will start using a variation of this code in my apps along with a "secret agent" that will notify me if it ever encounters a device with a status bar bigger than 25dp (if it's smaller or not at the top, it won't be a problem as it will get drawn behind the ActionBar).<br>
If such a thing happens, I will update this accordingly.
