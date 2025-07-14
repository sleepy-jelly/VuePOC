# VuePOC
simple vue3 with vite + spring boot project

vue3, vite, boot를 이용하여

OpenAI api를 사용한 
로그인, 회원가입, 채팅 서비스 (단순 챗봇 및 오디오 요약 기능) 기능 개발 POC

응답이 올때까지 전문 기다렸다가 한번에 전달하는 / getSimpleStrResponse   / jsp, html 등 적합
flux 배열로 전달하는/getStreamChatResponse                            / vue, react 동적인 ui처리가 편한 환경에 적합
flux 배열로 전달하는/getStreamAudioSummaryResponse                    / vue, react 동적인 ui처리가 편한 환경에 적합

이 핵심임






# VuePOC-Vue

This template should help get you started developing with Vue 3 in Vite.

## Recommended IDE Setup

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur).

## Type Support for `.vue` Imports in TS

TypeScript cannot handle type information for `.vue` imports by default, so we replace the `tsc` CLI with `vue-tsc` for type checking. In editors, we need [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) to make the TypeScript language service aware of `.vue` types.

## Customize configuration

See [Vite Configuration Reference](https://vite.dev/config/).

## Project Setup

```sh
npm install
```

### Compile and Hot-Reload for Development

```sh
npm run dev
```

### Type-Check, Compile and Minify for Production

```sh
npm run build
```

### Lint with [ESLint](https://eslint.org/)

```sh
npm run lint
```
