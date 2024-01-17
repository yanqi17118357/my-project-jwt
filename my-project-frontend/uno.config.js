// uno.config.js
import { defineConfig } from 'unocss'
import presetUno from '@unocss/preset-uno'

export default defineConfig({
  presets: [
    presetUno(),
  ],
  rules: [
    ['text-shadow-black', { 'text-shadow': '0 0 10px black' }],
  ],
})
