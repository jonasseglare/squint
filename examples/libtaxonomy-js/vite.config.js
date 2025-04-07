/// <reference types="vitest" />
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { visualizer } from 'rollup-plugin-visualizer';

export default defineConfig({
  test: {
	  include: ["public/**/**test.mjs", "public/**/**test.jsx"],
    browser: {
      provider: 'playwright', // or 'webdriverio'
      enabled: true,
      name: "chromium"
      // at least one instance is required
      //instances: [
      //  { browser: 'chromium' },
      //]
    }
  },
  plugins: [
	  react(),
	  visualizer({ open: false, filename: 'bundle-visualization.html' })
  ],
  build: {
	  lib: {
	    entry: ["js/index.jsx"],
	    name: "libtaxonomy",

	    // UMD = Universal module definition
	    // ES = Ecma Script
	    fileName: (format, entryName) => `libtaxonomy-${entryName}.${format}.js`,
	  }
  }
});
