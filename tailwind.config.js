/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
     './src/main/resources/templates/**/*.html',
     './src/main/resources/static/**/*.js'
  ],
    safelist: [
      'hover:-translate-y-1',
      'hover:-translate-y-2',
      'hover:scale-[1.01]',
      'hover:shadow-lg',
      'transition',
      'transform',
      'duration-200',
      'duration-300',
      'duration-500',
    ],
  theme: {
    extend: {},
  },
  plugins: [],
}

