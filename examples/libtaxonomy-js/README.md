## How to reproduce the bug

First install the node modules:
```
npm install
```

Run `bb reproduce_bug.bb reproduce-using-watch` and you will see something like
```
Compiled output:

var squint_core = await import('squint-cljs/core.js');
globalThis.libtaxonomy.db = globalThis.libtaxonomy.db || {};
globalThis.libtaxonomy = globalThis.libtaxonomy || {};
globalThis.libtaxonomy.my_component = globalThis.libtaxonomy.my_component || {};

globalThis namespace assignment order:
* Line 2: libtaxonomy.db
* Line 3: libtaxonomy
* Line 4: libtaxonomy.my_component

Detected 1 problems:
globalThis-assignment for 'libtaxonomy.db' on line 2 should not precede 'libtaxonomy' on line 3
```
In the above output, we see **one problem** detected.

Run `bb reproduce_bug.bb reproduce-using-compile` and you will see something like:

```
Compiled output:

var squint_core = await import('squint-cljs/core.js');
globalThis.user = globalThis.user || {};
globalThis.libtaxonomy = globalThis.libtaxonomy || {};
globalThis.libtaxonomy.my_component = globalThis.libtaxonomy.my_component || {};

globalThis namespace assignment order:
* Line 2: user
* Line 3: libtaxonomy
* Line 4: libtaxonomy.my_component

No problems found.
```
According to the above output, no problems were detected.

So it seems like this bug manifests itself with **watch**.
