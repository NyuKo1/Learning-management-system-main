module.exports = {
  content: ["./src/**/*.{html,ts}"],
  darkMode: 'class', // Включение поддержки темной темы через класс
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: "#86dad3",
          dark: "#005a81",
        },
        // Добавьте кастомные цвета для фонов
        bgMain: {
          light: "#ffffff",
          dark: "#121212",
        }
      },
    },
  },
  plugins: [],
  important: true,
};