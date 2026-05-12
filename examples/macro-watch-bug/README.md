# Squint bug: macro changes not picked up in watch mode

When running `squint watch`, changes to macros defined in a separate file are not reflected in files that use them — even after triggering a recompile of the consuming file.

## Setup

```
npm install
```

## Files

**src/macros.cljs** — defines a simple macro:

```clojure
(ns macros)

(defmacro greet [name]
  `(str "Hello, " ~name "!"))
```

**src/app.cljs** — uses the macro:

```clojure
(ns app
  (:require-macros [macros :refer [greet]]))

(js/console.log (greet "world"))
```

## Steps to reproduce

1. Start watch mode:

   ```
   npm run dev
   ```

2. Verify initial output in `app.mjs`:

   ```js
   console.log(`${"Hello, "}world${"!"}`);
   ```

3. Change `"Hello, "` to `"Goodbye, "` in `src/macros.cljs` and save.

4. Squint recompiles `macros.mjs`, but `app.mjs` still says `"Hello, "`.

5. Make a dummy edit to `src/app.cljs` (e.g. add a blank line) and save.

6. Squint recompiles `app.mjs`, but it **still** says `"Hello, "`.

## Expected behavior

After step 5 (or ideally step 3), `app.mjs` should contain `"Goodbye, "`.

## Actual behavior

The macro definition is cached in the watch process and never invalidated. Recompiling `app.cljs` reuses the stale macro.

**Note:** `squint compile src/app.cljs` (without watch) picks up the change correctly every time. The bug is specific to watch mode.
