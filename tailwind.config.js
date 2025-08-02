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
      'peer-valid:inline-block',
      'invalid:border-red-500',
      'invalid:focus:ring-red-500',
      'text-red-500',
      'text-green-500',
      'focus:ring-2',
      'focus:ring-blue-500',
      'rounded-md',
      'px-4',
      'py-2',
    ],
  theme: {
    extend: {},
  },
  plugins: [],
}

