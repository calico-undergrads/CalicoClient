Simplified Calico Plugin
========================
This file contains a list of things to look at
when creating a Calico plugin.

**Note On Version Control**: *All filed inside the `bin/` and `dist/` directories will be ignored, since the files in those directories can typically be compiled from the source which is versioned itself. If you want to add those files to your version control, edit the `.gitignore` files inside the root of the project and `dis/`.*

## Required Libraries & Other Resources
##### Java Libraries
Required libraries(jar files) should be placed in the `lib` folder of your project's root. Do not forger to include them as part of your Eclipse Project's External Libraries

##### Ant Build
`build.xml` builds the project auto-magically with ant.

1. Change `<property name="jarname" value="YOUR_PLUGIN_NAME_IDENTIFIER_HERE" />`
 * Use this value in the shell script below
2. Change `<project name="PLUGINNAME" default="run" basedir=".">`

##### Shell Scripts

Use the command `sh SHELL_SCRIPT_NAME.sh` to run the shell scripts from `terminal` or `cygwin`
*(If you are having trouble running that comamnd in windows, try changing the extensions to .bat and running `SHELL_SCRIPT_NAME.bat` from the command-prompt)*

**Edit the Shell Scripts with the name of your plugin that you used for the `jarname` in the `build.xml` file above**

* `DeployExamplePlugin.sh` - Just copies the plugin's jar file into the Calico Client Root `plugins` directory.
* `RunCalicoWithExamplePlugin.sh` - Calls `DeployExamplePlugin.sh` and **Builds and Runs** the Calico Client
* `UpdateCalicoJarInPlugins.sh` - Builds Calico Client and copies the updated jar into the plugin's lib folder (*Overriding an old version of the Calico Client used as a ref for the plugin*) **Edit the directory name of the plugin in the shell script to match actual plugin name directory name.**

**NOTE: The shell scripts use relative paths to work their magic. It is assumed that these shell scripts are inside the `plugins` directory of the root of the version control directory (*`plugins` dir outside of `trunk`*)**
## Icons & Graphic Files

Directory Stucture from project root: `/icons/calico/icnosets/calico/` - the second `calico` is the theme-name declared by `CalicoOptions.core.icontheme`.

* Include all image files inside this directory.
* Include the following properties file *(The plugin file name is defined in the plugin's `CalicoIconManager` class)*
    
`simplifiedpluginicontheme.properties`  contains:

    theme.name=Calico Icons
    theme.author.name=SDCL
    theme.author.url=http://sdcl.ics.uci.edu
    theme.author.email=none
    
    plugins.simplifiedplugin.customIcon=customIcon.png

Note that on the last line the string on the left-hand-side of the equals sign will be image file will be used inside the plugin's `calico.plugins.simplifiedPlugin.iconsets.CalicoIconManager` class to refer to that particular graphic.

## Plugin's Main Class
`SimplifiedPlugin` is the plugin's main class that initiates the necessary parts to make the plugin work. 
The main class must extend `calico.plugins.CalicoPlugin` and implement/override the following hooks:

##### Hooks:

`public void onPluginStart()`  this method is called when starting the plugin by Calico, it adds the button to the UI, and creates event listeners, etc.

*   Call super()
*   Set your plugin name: `PluginInfo.name = "SimplifiedPlugin"`
*   Set Icon Theme: `calico.plugins.simplifiedPlugin.iconsets.CalicoIconManager.setIconTheme(this.getClass(), CalicoOptions.core.icontheme);` (*See `CalicoIconManager` Below*)

--- 

`public void onPluginEnd()` this method is called by Calico before it shuts down, use it to do any necessary clean-up.

--- 

`public Class getNetworkCommandsClass()` this method returns the class containing a definition of the used network commands. (eg: `SimplifiedPluginNetworkCommands` in this case)

--- 

`public void handleCalicoEvent(int event, CalicoPacket p)` which is called on, to handle events. Note: Implementation should do a `switch` with known events defined inside `SimplifiedPluginNetworkCommands` and then call other applicable function to handle the actual event packet - `CalicoPacket`.

---
`public static void UI_send_command(int com, Object... params)` Called by the UI(``CustomSimplifiedPluginButton`) to create the command packet - `CalicoPacket` to send to the network.


## Network Commands
`SimplifiedPluginNetworkCommands` defines the commands for our plugin.

The class does not contains any methods.
The class serves as a wrapper that contains constants of type `int`. (*Ensure that you pick a range not already used by `calico.networking.netstuff.NetworkCommand` or other plugins unless you want to hook-on to other existing events.*)

## Icon Manager
`CalicoIconManager` is the class that is refered to in the `SimplifiedPlugin.onPluginStart()` that sets the icon files for the buttons created by the plugin. Most of the edits will occur inside the `setIconTheme` method.

`setIconTheme(Class<?> clazz, String name)` - Loads the icon theme. `name` will typically be "calico" and be the `calico` directory **inside** the `/icons/calico/iconsets` directory. the `clazz` is used to get and load the `.properties` file. *Make sure to set the correct name to the dot-properties file in the concatenated string being passed to `clazz.getResouceAsStream()`*

**Other Method Remain As Is - Fot the Time Being**
## Custom UI Components
#### A Simple Custom Button
`CustomSimplifiedPluginButton` is the class that is used to create a button on the Canavas Menu at the bottom of the screen. The class extends `calico.component.menus.CanavsMenuButton`. Other custom components can extend other UI classes inside the `calico.components.*` package namespace.

`public CustomSimplifiedPluginButton(long canvasId)` accepts a canvas id, and stores it in the inherited class variable `cuid`. 
In this method also set the `iconString` value with the left-hand-side value specified in the dot-properties file inside the `icons/...` directoru which is then used to try and `setImage` for the button using the `calico.plugins.simplifiedPlugin.iconsets.CalicoIconManager` from our plugin to get the Icon Image.

`public void actionMouseClicked(InputEventInfo event)` This method listens for mouse events, and takes action. This method calls the main class' `SimplifiedPlugin.UI_send_command` and passes a newly generared `uuid` for any new element that will be created, current canavs uuid `cuuid`, etc.

`public void highlight_off()` and `public void highlight_on()` are part of the UI, and handle button states. **Copy code as is**


##### A Simple Custom Component
`SimplifiedElement` is a custom calico element that `extends` `calico.component.CGroup`.
`CGroup` is an elementary object type used in calico used to create a group of "objects".

`SimplifiedElement` constructor must accept be passed a unique `uuid`, canvas id `cuuid`, parent id `puid` (**optional** - `0L` is default), and `isperm` (**optional** - `false` is default). then `super(params...)` must be called with the appropriate parameters. The constructor also sets `networkLoadCommand` for this particular UI element (Refer to `SimplePluginNetworkCommands`)

`public void setInputHandler()` sets a custom inpuput handler by calling `CalicoInputManager.addCustomInputHandler` and passing the groups uuid, and an instance of the input handler for this custom component.

`public CalicoPacket[] getUpdatePackets(long uuid, long cuid, long puid, int dx, int dy, boolean captureChildren)` - This method creates the packet for saving the CGroup element.
      
`public int get_signature()` gets the int value which identifies this CGroup element (see Nick for explanation?)


## Custom Input Handlers
`SimplifiedInputHandler` `extends` `CGroupInputHandler`. The constructor accepts a `uuid` of a `CGroup` element as a parameter that is passed to the parent class. (This class is created in `SimplifiedElement.setInputHandler()`)

See Nick for explanation of `public void actionStrokeToAnotherGroup(long strokeUID, long targetGroup)` or see parent class declaration.

## Custom Calico Controllers

`SimplifiedController` is full of `static` methods which are called by other parts of the application (Mostly after some UI event occurs).

`create_custom_scrap(long new_uuid, long cuuid, int x, int y)` is called by the main class of the plug in after a particular event defined in `SimplifiedPluginNetworkCommands` has been matched. 

#### How it Works & Where UUID, CUUID, X and Y come from:

1. The `new_uuid` and `cuuid` were created in the UI class by the `SimplifiedElement.actionMouseClicked` method when the mouse is clicked,
* The UI class passes the new information to `SimplifiedPlugin.UI_send_command`*(main class)* to be put into a packet and trigger the next function call.
* Calico then passed the packet into the `SimplifiedPlugin.handleCalicoEvent` *(main class)* to be processed
 * If a match to a command is found, the appropriate method in `SimplifiedPlugin` class is called. Which then rewinds and extracts the data, which is **then** passed to the Controller to handle (eg: `SimplifiedController.create_custom_scrap(...)`).

`public static void no_notify_create_custom_scrap(long uuid, long cuuid, int x, int y, String optText)` is a function that show how objects can be drawn to the canvas, using `GeneralPath`, `Rectangle`, `Polygon`, and `CGroup` to group objects.

#### Utility Methods
*See Nick for further explanations*
`public static void no_notify_create_custom_scrap_bootstrap(long uuid, long cuuid, CGroup group, Polygon p, String optText)` Deligate construction of elements to other methods in the class.

`public static void no_notify_start(long uuid, long cuid, long puid, boolean isperm, CGroup customScrap)`  - Starts the creation of any of the activity diagram scrap, checks for existance and and draws to canvas.

`public static void create_custom_shape(long uuid, Polygon p)` Iterates through `Polygon`'s points. 

---


## Usefull Classes & Methods in Calico Client
* `Calico` 
   * `uuid()` generates a unique `uuid` for an object.
* `CalicoOptions`
   *  Contains sub-classes with setting information, eg: download folder, icon-theme, input-settings, 
* `CalicoDataStore`
  *  Contains usefull information like, screen size, server host & port, current pen-color, thickness, etc.
* `CCanvasController` - Contains usefull methods pertainign to current canvas;     
    * `getCurrentUUID`,
    * `notifyContentChanged`

