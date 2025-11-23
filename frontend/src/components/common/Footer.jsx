const Footer = () => {
  return (
    <footer className="bg-gray-800 text-white mt-auto">
      <div className="container mx-auto px-4 py-6">
        <div className="flex justify-between items-center">
          <div>
            <p className="text-sm">
              © 2024 E-Commerce Platform. All rights reserved.
            </p>
          </div>
          <div className="flex space-x-4">
            <a href="#" className="hover:text-primary-400">About</a>
            <a href="#" className="hover:text-primary-400">Contact</a>
            <a href="#" className="hover:text-primary-400">Privacy</a>
          </div>
        </div>
      </div>
    </footer>
  )
}

export default Footer
