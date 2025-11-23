import { useState, useRef, useEffect } from 'react'

const Chatbot = () => {
  const [isOpen, setIsOpen] = useState(false)
  const [messages, setMessages] = useState([
    { text: "Hi! I'm Smart Shop Assistant. How can I help you today?", sender: 'bot' }
  ])
  const [inputMessage, setInputMessage] = useState('')
  const messagesEndRef = useRef(null)

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }

  useEffect(() => {
    scrollToBottom()
  }, [messages])

  const quickReplies = [
    'Track my order',
    'Shipping info',
    'Return policy',
    'Payment methods',
    'Contact support'
  ]

  const getBotResponse = (userMessage) => {
    const message = userMessage.toLowerCase()

    // Product related
    if (message.includes('product') || message.includes('item') || message.includes('buy')) {
      return "We have a wide range of products including Electronics, Clothing, Sports items, and Accessories. You can browse all products by clicking on 'Products' in the menu. Is there something specific you're looking for?"
    }

    // Shipping
    if (message.includes('ship') || message.includes('deliver')) {
      return "We offer FREE shipping on all orders! Delivery typically takes 3-5 business days. You can track your order status from the Orders page after logging in."
    }

    // Payment
    if (message.includes('payment') || message.includes('pay') || message.includes('card') || message.includes('upi')) {
      return "We accept multiple payment methods: Credit/Debit Cards, UPI, and Cash on Delivery (COD). All payments are 100% secure and encrypted."
    }

    // Return/Refund
    if (message.includes('return') || message.includes('refund') || message.includes('exchange')) {
      return "We have a 7-day return policy. If you're not satisfied with your purchase, you can return it within 7 days for a full refund. The product must be unused and in original packaging."
    }

    // Order tracking
    if (message.includes('track') || message.includes('order status') || message.includes('where is my order')) {
      return "To track your order, please log in to your account and go to the 'Orders' section. You'll see all your orders with their current status."
    }

    // Account
    if (message.includes('account') || message.includes('login') || message.includes('register') || message.includes('sign up')) {
      return "You can create an account by clicking 'Sign Up' in the top menu. If you already have an account, click 'Login'. Having an account helps you track orders and save your preferences!"
    }

    // Cart
    if (message.includes('cart') || message.includes('add to cart')) {
      return "You can add products to your cart without logging in! Just click 'Add to Cart' on any product. Your cart is saved in your browser, so you won't lose items even if you refresh the page."
    }

    // Contact
    if (message.includes('contact') || message.includes('support') || message.includes('help') || message.includes('email') || message.includes('phone')) {
      return "You can reach our support team at:\n📧 Email: support@smartshop.com\n📞 Phone: +91-1800-123-4567\nWe're available 24/7 to help you!"
    }

    // Discount/Offers
    if (message.includes('discount') || message.includes('offer') || message.includes('coupon') || message.includes('sale')) {
      return "We regularly have special offers and discounts! Check our homepage for current deals. Sign up for our newsletter to get exclusive discount codes delivered to your email."
    }

    // Price
    if (message.includes('price') || message.includes('cost') || message.includes('how much')) {
      return "All our products are competitively priced! You can see the price on each product page. We display prices in Indian Rupees (₹). Free shipping is included on all orders!"
    }

    // Greetings
    if (message.includes('hi') || message.includes('hello') || message.includes('hey')) {
      return "Hello! Welcome to Smart Shop! 👋 How can I assist you today? Feel free to ask about products, shipping, payments, or anything else!"
    }

    // Thanks
    if (message.includes('thank') || message.includes('thanks')) {
      return "You're welcome! Happy to help! 😊 Is there anything else you'd like to know?"
    }

    // Default response
    return "I'm here to help! You can ask me about:\n• Products and categories\n• Shipping and delivery\n• Payment methods\n• Returns and refunds\n• Order tracking\n• Account creation\n\nWhat would you like to know?"
  }

  const handleSendMessage = () => {
    if (inputMessage.trim() === '') return

    // Add user message
    const userMsg = { text: inputMessage, sender: 'user' }
    setMessages(prev => [...prev, userMsg])

    // Get bot response
    setTimeout(() => {
      const botResponse = getBotResponse(inputMessage)
      const botMsg = { text: botResponse, sender: 'bot' }
      setMessages(prev => [...prev, botMsg])
    }, 500)

    setInputMessage('')
  }

  const handleQuickReply = (reply) => {
    setInputMessage(reply)
    handleSendMessage()
  }

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSendMessage()
    }
  }

  return (
    <>
      {/* Chat Button */}
      {!isOpen && (
        <button
          onClick={() => setIsOpen(true)}
          className="fixed bottom-6 right-6 bg-gradient-to-r from-blue-600 to-blue-500 text-white p-4 rounded-full shadow-lg hover:shadow-xl transition-all z-50 flex items-center justify-center"
        >
          <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
          </svg>
          <span className="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
            1
          </span>
        </button>
      )}

      {/* Chat Window */}
      {isOpen && (
        <div className="fixed bottom-6 right-6 w-96 h-[600px] bg-white rounded-lg shadow-2xl flex flex-col z-50">
          {/* Header */}
          <div className="bg-gradient-to-r from-blue-600 to-blue-500 text-white p-4 rounded-t-lg flex justify-between items-center">
            <div className="flex items-center">
              <div className="w-10 h-10 bg-white rounded-full flex items-center justify-center mr-3">
                <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
              </div>
              <div>
                <h3 className="font-semibold">Smart Shop Assistant</h3>
                <p className="text-xs text-blue-100">Online • Always here to help</p>
              </div>
            </div>
            <button
              onClick={() => setIsOpen(false)}
              className="text-white hover:text-blue-200 transition-colors"
            >
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          {/* Messages */}
          <div className="flex-1 overflow-y-auto p-4 space-y-4 bg-gray-50">
            {messages.map((msg, index) => (
              <div
                key={index}
                className={`flex ${msg.sender === 'user' ? 'justify-end' : 'justify-start'}`}
              >
                <div
                  className={`max-w-[80%] p-3 rounded-lg ${
                    msg.sender === 'user'
                      ? 'bg-blue-600 text-white rounded-br-none'
                      : 'bg-white text-gray-800 rounded-bl-none shadow'
                  }`}
                >
                  <p className="text-sm whitespace-pre-line">{msg.text}</p>
                </div>
              </div>
            ))}
            <div ref={messagesEndRef} />
          </div>

          {/* Quick Replies */}
          {messages.length <= 2 && (
            <div className="px-4 py-2 bg-gray-50 border-t">
              <p className="text-xs text-gray-600 mb-2">Quick questions:</p>
              <div className="flex flex-wrap gap-2">
                {quickReplies.map((reply, index) => (
                  <button
                    key={index}
                    onClick={() => handleQuickReply(reply)}
                    className="text-xs bg-white border border-gray-300 text-gray-700 px-3 py-1 rounded-full hover:bg-blue-50 hover:border-blue-300 transition-colors"
                  >
                    {reply}
                  </button>
                ))}
              </div>
            </div>
          )}

          {/* Input */}
          <div className="p-4 border-t bg-white rounded-b-lg">
            <div className="flex space-x-2">
              <input
                type="text"
                value={inputMessage}
                onChange={(e) => setInputMessage(e.target.value)}
                onKeyPress={handleKeyPress}
                placeholder="Type your message..."
                className="flex-1 px-4 py-2 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <button
                onClick={handleSendMessage}
                className="bg-blue-600 text-white p-2 rounded-full hover:bg-blue-700 transition-colors"
              >
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8" />
                </svg>
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  )
}

export default Chatbot
