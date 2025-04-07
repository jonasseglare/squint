## Repo to reproduce a bug

Perform these steps:
```
npm install
bb dev
```

Open the browser at `localhost:5173` or whatever address is displayed in the console and observe
```
Uncaught TypeError: globalThis.libtaxonomy is undefined
    <anonymous> my_component.jsx:2
```

## More details

The problematic file is `public/js/libtaxonomy/my_component.jsx`.

It contains this code:
```
var squint_core = await import('squint-cljs/core.js');
globalThis.libtaxonomy.db = globalThis.libtaxonomy.db || {};
globalThis.libtaxonomy = globalThis.libtaxonomy || {};
globalThis.libtaxonomy.my_component = globalThis.libtaxonomy.my_component || {};
var { useState } = (await import ('react'));
globalThis.libtaxonomy.my_component.useState = useState;
if ((typeof libtaxonomy !== 'undefined') && (typeof libtaxonomy.my_component !==
 'undefined') && (typeof libtaxonomy.my_component.x !== 'undefined')) {
} else {
var x = 10;

...
```

Note that the line

```
globalThis.libtaxonomy.db = globalThis.libtaxonomy.db || {};
```


comes before

```
globalThis.libtaxonomy = globalThis.libtaxonomy || {};
```

